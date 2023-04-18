package cz.maku.mommons.worker;

import cz.maku.mommons.worker.annotation.BukkitCommand;
import cz.maku.mommons.worker.annotation.BukkitEvent;

public class BukkitWorkerMethod extends WorkerExecutable {

    public BukkitWorkerMethod(WorkerExecutable workerMethod) {
        super(workerMethod.getExecutable(), workerMethod.getObject(), workerMethod.getLogger());
    }

    public boolean isCommand() {
        return getExecutable().isAnnotationPresent(BukkitCommand.class);
    }

    public boolean isEvent() {
        return getExecutable().isAnnotationPresent(BukkitEvent.class);
    }

}
