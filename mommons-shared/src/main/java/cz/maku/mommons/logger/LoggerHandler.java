package cz.maku.mommons.logger;

import cz.maku.mommons.utils.ConsoleColors;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoggerHandler extends Handler {

    private final Class<?> clazz;

    public LoggerHandler() {
        this(null);
    }

    public LoggerHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void publish(LogRecord record) {
        String className;
        if (clazz == null) {
            className = record.getSourceClassName();
        } else {
            className = clazz.getName();
        }
        record.setLoggerName(String.format("%s%s%s - %s%s%s", ConsoleColors.CYAN_BRIGHT, Thread.currentThread().getName(), ConsoleColors.WHITE_BRIGHT, ConsoleColors.CYAN_BRIGHT, className, ConsoleColors.WHITE_BRIGHT));
    }

    @Override
    public void flush() {
        System.out.flush();
    }

    @Override
    public void close() throws SecurityException {
        System.out.close();
    }

}
