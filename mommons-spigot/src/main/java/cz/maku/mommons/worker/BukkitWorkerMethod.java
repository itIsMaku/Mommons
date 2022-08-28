package cz.maku.mommons.worker;

import cz.maku.mommons.worker.annotation.BukkitCommand;
import cz.maku.mommons.worker.annotation.BukkitEvent;

public class BukkitWorkerMethod extends WorkerMethod {

    public BukkitWorkerMethod(WorkerMethod workerMethod) {
        super(workerMethod.getMethod(), workerMethod.getObject(), workerMethod.getLogger());
    }

    public boolean isCommand() {
        return getMethod().isAnnotationPresent(BukkitCommand.class);
    }

    public boolean isEvent() {
        return getMethod().isAnnotationPresent(BukkitEvent.class);
    }

}
