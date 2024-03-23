package cz.maku.mommons.worker;

import org.jetbrains.annotations.Nullable;

public final class BungeeWorkerReceiver {

    @Nullable
    public static <T> T getService(Worker worker, Class<T> clazz) {
        if (worker == null) return null;
        return (T) worker.getWorkerClasses().get(clazz).getObject();
    }
}
