package cz.maku.mommons.bukkit.scheduler;

import cz.maku.mommons.loader.MommonsLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public final class Schedulers {

    public static final BukkitScheduler BUKKIT_SCHEDULER = Bukkit.getScheduler();
    public static final JavaPlugin SCHEDULER_SOURCE = MommonsLoader.getPlugin();

    public static void repeat(Consumer<BukkitTask> consumer, long delay, long period) {
        BUKKIT_SCHEDULER.runTaskTimer(SCHEDULER_SOURCE, consumer, delay, period);
    }

    public static void repeatBukkitAsync(Consumer<BukkitTask> consumer, long delay, long period) {
        BUKKIT_SCHEDULER.runTaskTimerAsynchronously(SCHEDULER_SOURCE, consumer, delay, period);
    }

    public static void later(Consumer<BukkitTask> consumer, long delay) {
        BUKKIT_SCHEDULER.runTaskLater(SCHEDULER_SOURCE, consumer, delay);
    }

    public static void laterBukkitAsync(Consumer<BukkitTask> consumer, long delay) {
        BUKKIT_SCHEDULER.runTaskLaterAsynchronously(SCHEDULER_SOURCE, consumer, delay);
    }

    public static void run(Consumer<BukkitTask> consumer) {
        BUKKIT_SCHEDULER.runTask(SCHEDULER_SOURCE, consumer);
    }

    public static void runBukkitAsync(Consumer<BukkitTask> consumer) {
        BUKKIT_SCHEDULER.runTaskAsynchronously(SCHEDULER_SOURCE, consumer);
    }
}
