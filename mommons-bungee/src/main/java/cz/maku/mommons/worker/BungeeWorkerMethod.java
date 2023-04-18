package cz.maku.mommons.worker;

import cz.maku.mommons.worker.annotation.BungeeCommand;
import cz.maku.mommons.worker.annotation.BungeeEvent;

public class BungeeWorkerMethod extends WorkerExecutable {

    public BungeeWorkerMethod(WorkerExecutable workerMethod) {
        super(workerMethod.getExecutable(), workerMethod.getObject(), workerMethod.getLogger());
    }

    public boolean isCommand() {
        return getExecutable().isAnnotationPresent(BungeeCommand.class);
    }

    public boolean isEvent() {
        return getExecutable().isAnnotationPresent(BungeeEvent.class);
    }

}
