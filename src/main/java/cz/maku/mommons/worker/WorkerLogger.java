package cz.maku.mommons.worker;

import cz.maku.mommons.loader.MommonsLoader;
import cz.maku.mommons.worker.type.WorkerLoggerType;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;

import static cz.maku.mommons.worker.type.WorkerLoggerType.ERROR;
import static cz.maku.mommons.worker.type.WorkerLoggerType.INFO;

public class WorkerLogger {

    protected static String format(WorkerLoggerType workerLoggerType) {
        return "[Mommons] " + workerLoggerType.getPrefix() + "- " + LocalDateTime.now();
    }

    @SafeVarargs
    public static <T> void error(T... content) {
        blank(format(ERROR));
        for (T t : content) {
            blank(ERROR.getPrefix() + t.toString());
        }
    }

    @SafeVarargs
    public static <T> void info(T... content) {
        for (T t : content) {
            blank(INFO.getPrefix() + t.toString());
        }
    }

    public static <T> void blank(T... content) {
        for (T t : content) {
            Bukkit.getConsoleSender().sendMessage(t.toString());
        }
    }

    public static void error(Exception e) {
        e.printStackTrace();
    }

}
