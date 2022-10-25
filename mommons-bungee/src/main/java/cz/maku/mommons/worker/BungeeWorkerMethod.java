package cz.maku.mommons.worker;

import cz.maku.mommons.worker.annotation.BungeeCommand;
import cz.maku.mommons.worker.annotation.BungeeEvent;

public class BungeeWorkerMethod extends WorkerMethod {

    public BungeeWorkerMethod(WorkerMethod workerMethod) {
        super(workerMethod.getMethod(), workerMethod.getObject(), workerMethod.getLogger());
    }

    public boolean isCommand() {
        return getMethod().isAnnotationPresent(BungeeCommand.class);
    }

    public boolean isEvent() {
        return getMethod().isAnnotationPresent(BungeeEvent.class);
    }

}
