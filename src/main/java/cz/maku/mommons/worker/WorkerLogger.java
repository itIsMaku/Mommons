package cz.maku.mommons.worker;

import cz.maku.mommons.worker.type.WorkerLoggerType;

import java.time.LocalDateTime;

import static cz.maku.mommons.worker.type.WorkerLoggerType.*;

public class WorkerLogger {

    protected static String format(WorkerLoggerType workerLoggerType) {
        return workerLoggerType.getPrefix() + " - " + LocalDateTime.now();
    }

    @SafeVarargs
    public static <T> void error(T... content) {
        System.out.println(format(ERROR));
        for (T t : content) {
            System.out.println(ERROR.getPrefix() + t.toString());
        }
    }

    public static <T> void blank(T... content) {
        for (T t : content) {
            System.out.println(t);
        }
    }

    public static void error(Exception e) {
        error(e.getStackTrace());
    }

}
