package cz.maku.mommons.cache;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Queue {

    /*private final Map<QueueTask<?>> queueTasks;

    public Queue(Map<QueueTask<?>> queueTasks) {
        this.queueTasks = queueTasks;
    }

    public Queue() {
        this(Lists.newCopyOnWriteArrayList());
    }

    public <V> void add(QueueTask<V> queueTask) {
        queueTasks.add(queueTask);
    }

    public void start() {
        for (QueueTask<?> queueTask : queueTasks) {
            queueTask.complete();
        }
    }*/
}
