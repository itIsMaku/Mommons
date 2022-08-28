package cz.maku.mommons.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public final class Timers {

    public static void repeat(Consumer<TimerTask> taskConsumer, long delay, long period) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                taskConsumer.accept(this);
            }
        };
        timer.schedule(task, delay, period);
    }

}
