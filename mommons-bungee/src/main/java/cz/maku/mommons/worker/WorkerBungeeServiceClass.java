package cz.maku.mommons.worker;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.bungee.Schedulers;
import cz.maku.mommons.plugin.MommonsPluginBungee;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.BungeeCommand;
import cz.maku.mommons.worker.annotation.BungeeEvent;
import cz.maku.mommons.worker.annotation.Repeat;
import cz.maku.mommons.worker.annotation.sql.Download;
import lombok.SneakyThrows;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WorkerBungeeServiceClass extends WorkerServiceClass {

    private final BungeeWorker worker;
    private final Map<WorkerExecutable, ScheduledTask> tasks;

    public WorkerBungeeServiceClass(BungeeWorker bungeeWorker, WorkerServiceClass workerServiceClass, Map<WorkerExecutable, ScheduledTask> tasks) {
        super(workerServiceClass.getWorker(), workerServiceClass.getService(), workerServiceClass.getObject(), workerServiceClass.getMethods(), workerServiceClass.getFields(), workerServiceClass.getLogger(), Maps.newConcurrentMap());
        this.worker = bungeeWorker;
        this.tasks = tasks;
    }

    @Override
    protected void handleSqlDownload(WorkerExecutable workerMethod, List<Object> params) {
        Download download = workerMethod.getExecutable().getAnnotation(Download.class);
        ScheduledTask scheduledTask = Schedulers.repeatAsync(() -> {
            try {
                workerMethod.invoke(new Object[]{MySQL.getApi().query(download.table(), download.query())});
                params.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, download.delay(), download.period(), TimeUnit.MILLISECONDS);
        if (!tasks.containsKey(workerMethod)) {
            tasks.put(workerMethod, scheduledTask);
        }
    }

    @Override
    protected void handleRepeatTask(WorkerExecutable workerMethod, List<Object> params) {
        Repeat repeat = workerMethod.getExecutable().getAnnotation(Repeat.class);
        ScheduledTask scheduledTask = Schedulers.repeatAsync(() -> {
            try {
                //workerMethod.invoke(params.toArray());
                workerMethod.invoke(new Object[]{});
                params.clear();
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }, repeat.delay(), repeat.period(), TimeUnit.SECONDS);
        if (!tasks.containsKey(workerMethod)) {
            tasks.put(workerMethod, scheduledTask);
        }
    }

    @Override
    protected void nextHandlers(WorkerExecutable workerMethod, List<Object> params) {
        BungeeWorkerMethod bungeeWorkerMethod = new BungeeWorkerMethod(workerMethod);
        if (getService().commands() && bungeeWorkerMethod.isCommand()) {
            BungeeCommand command = bungeeWorkerMethod.getExecutable().getAnnotation(BungeeCommand.class);
            ProxyServer.getInstance().getPluginManager().registerCommand(MommonsPluginBungee.getPlugin(), new Command(command.value(), command.permission(), command.aliases()) {
                @SneakyThrows
                @Override
                public void execute(CommandSender sender, String[] args) {
                    CommandSender commandSender = null;
                    String[] commandArgs = null;
                    Type argsType = new TypeToken<String[]>() {
                    }.getType();
                    for (Parameter parameter : bungeeWorkerMethod.getExecutable().getParameters()) {
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
                    bungeeWorkerMethod.invoke(params.toArray());
                    params.clear();
                }
            });
            return;
        }
        if (getService().listener() && bungeeWorkerMethod.isEvent()) {
            BungeeEvent eventAnnotation = bungeeWorkerMethod.getExecutable().getAnnotation(BungeeEvent.class);
            ProxyServer.getInstance().getPluginManager().registerListener(MommonsPluginBungee.getPlugin(), new Listener() {
                @EventHandler
                public void onEvent(Event event) {
                    if (event.getClass().isInstance(eventAnnotation.value())) {
                        params.add(event.getClass().cast(eventAnnotation.value()));
                        try {
                            bungeeWorkerMethod.invoke(params.toArray());
                            params.clear();
                        } catch (InvocationTargetException | IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
