package cz.maku.mommons.worker;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.bukkit.scheduler.Schedulers;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.BukkitCommand;
import cz.maku.mommons.worker.annotation.BukkitEvent;
import cz.maku.mommons.worker.annotation.Repeat;
import cz.maku.mommons.worker.annotation.sql.Download;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class WorkerBukkitServiceClass extends WorkerServiceClass {

    private final BukkitWorker worker;
    private final Map<WorkerMethod, BukkitTask> tasks;

    public WorkerBukkitServiceClass(BukkitWorker bukkitWorker, WorkerServiceClass workerServiceClass, Map<WorkerMethod, BukkitTask> tasks) {
        super(workerServiceClass.getWorker(), workerServiceClass.getService(), workerServiceClass.getObject(), workerServiceClass.getMethods(), workerServiceClass.getFields(), workerServiceClass.getLogger(), Maps.newConcurrentMap());
        this.worker = bukkitWorker;
        this.tasks = tasks;
    }

    @Override
    protected void handleSqlDownload(WorkerMethod workerMethod, List<Object> params) {
        Download download = workerMethod.getMethod().getAnnotation(Download.class);
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
    }

    @Override
    protected void handleRepeatTask(WorkerMethod workerMethod, List<Object> params) {
        Repeat repeat = workerMethod.getMethod().getAnnotation(Repeat.class);
        boolean usedTaskParameter = false;
        for (Parameter parameter : workerMethod.getMethod().getParameters()) {
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
    }

    @Override
    protected void nextHandlers(WorkerMethod workerMethod, List<Object> params) {
        BukkitWorkerMethod bukkitWorkerMethod = new BukkitWorkerMethod(workerMethod);
        if (getService().commands() && bukkitWorkerMethod.isCommand()) {
            BukkitCommand command = bukkitWorkerMethod.getMethod().getAnnotation(BukkitCommand.class);
            registerCommand(command.fallbackPrefix(), new Command(command.value(), command.description(), command.usage(), Arrays.asList(command.aliases())) {
                @SneakyThrows
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
                    CommandSender commandSender = null;
                    String[] commandArgs = null;
                    Type argsType = new TypeToken<String[]>() {
                    }.getType();
                    for (Parameter parameter : bukkitWorkerMethod.getMethod().getParameters()) {
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
                    bukkitWorkerMethod.invoke(params.toArray());
                    params.clear();
                    return false;
                }
            });
            return;
        }
        if (getService().listener() && bukkitWorkerMethod.isEvent()) {
            BukkitEvent eventAnnotation = bukkitWorkerMethod.getMethod().getAnnotation(BukkitEvent.class);
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
        }
    }

    @SneakyThrows
    private void registerCommand(String fallbackPrefix, Command cmd) {
        Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        commandMap.register(fallbackPrefix, cmd);
    }
}
