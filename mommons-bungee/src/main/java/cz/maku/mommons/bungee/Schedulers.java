package cz.maku.mommons.bungee;

import cz.maku.mommons.plugin.MommonsPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.concurrent.TimeUnit;

public final class Schedulers {

    public static final TaskScheduler TASK_SCHEDULER = ProxyServer.getInstance().getScheduler();
    public static final Plugin SCHEDULER_SOURCE = MommonsPlugin.getPlugin();

    public static ScheduledTask runAsync(Runnable task) {
        return TASK_SCHEDULER.runAsync(SCHEDULER_SOURCE, task);
    }

    public static ScheduledTask laterAsync(Runnable task, long delay, TimeUnit unit) {
        return TASK_SCHEDULER.schedule(SCHEDULER_SOURCE, task, delay, unit);
    }

    public static ScheduledTask repeatAsync(Runnable task, long delay, long period, TimeUnit unit) {
        return TASK_SCHEDULER.schedule(SCHEDULER_SOURCE, task, delay, period, unit);
    }
}
