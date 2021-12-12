package cz.maku.mommons.worker;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.bukkit.scheduler.Schedulers;
import cz.maku.mommons.loader.MommonsLoader;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.BukkitCommand;
import cz.maku.mommons.worker.annotation.BukkitEvent;
import cz.maku.mommons.worker.annotation.Repeat;
import cz.maku.mommons.worker.annotation.Service;
import cz.maku.mommons.worker.annotation.sql.Download;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Getter
public class WorkerServiceClass {

    private final Worker worker;
    private final Service service;
    private final Object object;
    private final Map<String, WorkerMethod> methods;
    private final Map<String, WorkerField> fields;
    private final Map<WorkerMethod, BukkitTask> tasks;
    private final Logger logger;

    public WorkerServiceClass(Worker worker, Service service, Object object, Map<String, WorkerMethod> methods, Map<String, WorkerField> fields, Map<WorkerMethod, BukkitTask> tasks) {
        this.worker = worker;
        this.service = service;
        this.object = object;
        this.methods = methods;
        this.fields = fields;
        this.tasks = tasks;
        this.logger = MommonsLoader.getPlugin().getLogger();
    }

    public WorkerServiceClass(Worker worker, Service service, Object object) {
        this(worker, service, object, Maps.newConcurrentMap(), Maps.newConcurrentMap(), Maps.newConcurrentMap());
    }

    @SneakyThrows
    public void initializeFields() {
        for (WorkerField workerField : fields.values()) {
            Field field = workerField.getField();
            if (workerField.isLoad()) {
                Class<?> fieldType = field.getType();
                if (worker.getServices().containsKey(fieldType)) {
                    if (worker.getServices().get(fieldType) == null) {
                        Object object = fieldType.newInstance();
                        worker.getServices().put(fieldType, object);
                        worker.initializeClass(fieldType, object);
                        workerField.setValue(object);
                    } else {
                        workerField.setValue(worker.getServices().get(fieldType));
                    }
                } else if (worker.getSpecialServices().containsKey(fieldType)) {
                    if (worker.getSpecialServices().get(fieldType) == null) {
                        Object object = fieldType.newInstance();
                        worker.getSpecialServices().put(fieldType, object);
                        worker.initializeClass(fieldType, object);
                        workerField.setValue(object);
                    } else {
                        workerField.setValue(worker.getSpecialServices().get(fieldType));
                    }
                } else {
                    logger.severe("Cannot @Load class " + field.getName() + ". Maybe is it Service?");
                }
            }
        }
    }

    @SneakyThrows
    public void initializeMethods() {
        for (WorkerMethod workerMethod : methods.values()) {
            Method method = workerMethod.getMethod();
            List<Object> params = new ArrayList<>(Arrays.asList(workerMethod.getLoadParameters(worker)));
            if (workerMethod.isInit()) {
                if (workerMethod.isAnotherThread()) {
                    new Thread(() -> {
                        try {
                            workerMethod.invoke(params.toArray());
                            params.clear();
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    workerMethod.invoke(params.toArray());
                    params.clear();
                }
                continue;
            }
            if (service.sql()) {
                if (workerMethod.isSqlDownload()) {
                    Download download = method.getAnnotation(Download.class);
                    if (workerMethod.isAsync()) {
                        Bukkit.getScheduler().runTaskTimerAsynchronously(worker.getJavaPlugin(), () -> {
                            try {
                                workerMethod.invoke(new Object[]{MySQL.getApi().queryAsync(download.table(), download.query())});
                                params.clear();
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }, download.delay(), download.period());
                    } else {
                        Bukkit.getScheduler().runTaskTimer(worker.getJavaPlugin(), () -> {
                            try {
                                workerMethod.invoke(new Object[]{MySQL.getApi().query(download.table(), download.query())});
                                params.clear();
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }, download.delay(), download.period());
                    }
                    continue;
                }
            }
            if (service.scheduled() && workerMethod.isRepeatTask()) {
                Repeat repeat = method.getAnnotation(Repeat.class);
                boolean usedTaskParameter = false;
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.getType().equals(BukkitTask.class)) {
                        usedTaskParameter = true;
                    }
                }
                boolean finalUsedTaskParameter = usedTaskParameter;
                Consumer<BukkitTask> consumer = task -> {
                    try {
                        if (finalUsedTaskParameter) {
                            params.add(task);
                        }
                        workerMethod.invoke(params.toArray());
                        params.clear();
                        if (!tasks.containsKey(workerMethod)) {
                            tasks.put(workerMethod, task);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                };
                if (workerMethod.isAsync()) {
                    Schedulers.repeatBukkitAsync(consumer, repeat.delay(), repeat.period());
                } else {
                    Schedulers.repeat(consumer, repeat.delay(), repeat.period());
                }
                continue;
            }
            if (service.commands() && workerMethod.isCommand()) {
                BukkitCommand command = method.getAnnotation(BukkitCommand.class);
                registerCommand(command.fallbackPrefix(), new Command(command.value(), command.description(), command.usage(), Arrays.asList(command.aliases())) {
                    @SneakyThrows
                    @Override
                    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
                        CommandSender commandSender = null;
                        String[] commandArgs = null;
                        Type argsType = new TypeToken<String[]>() {
                        }.getType();
                        for (Parameter parameter : method.getParameters()) {
                            if (parameter.getParameterizedType().equals(argsType)) {
                                commandArgs = args;
                            }
                            if (parameter.getType().equals(CommandSender.class)) {
                                commandSender = sender;
                            }
                        }
                        if (commandSender == null) {
                            throw new IllegalArgumentException("CommandSender is required in @BukkitCommand method.");
                        }
                        params.add(commandSender);
                        if (commandArgs != null) {
                            params.add(commandArgs);
                        }
                        workerMethod.invoke(params.toArray());
                        params.clear();
                        return false;
                    }
                });
                continue;
            }
            if (service.listener() && workerMethod.isEvent()) {
                BukkitEvent eventAnnotation = method.getAnnotation(BukkitEvent.class);
                Listener listener = new Listener() {
                };
                worker.getJavaPlugin().getServer().getPluginManager().registerEvent(eventAnnotation.value(), listener, eventAnnotation.priority(), (l, e) -> {
                    params.add(e);
                    try {
                        workerMethod.invoke(params.toArray());
                        params.clear();
                    } catch (InvocationTargetException | IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }, worker.getJavaPlugin(), false);
                continue;
            }
        }
    }

    @SneakyThrows
    private void registerCommand(String fallbackPrefix, Command cmd) {
        Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        commandMap.register(fallbackPrefix, cmd);
    }

    public void destroy() {

    }

}
