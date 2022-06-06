package cz.maku.mommons.worker;

import cz.maku.mommons.loader.MommonsLoader;
import cz.maku.mommons.worker.plugin.WorkerPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class WorkerReceiver {

    @Nullable
    public static Worker getWorker(Class<? extends WorkerPlugin> pluginClass) {
        for (Method method : pluginClass.getMethods()) {
            if (method.getName().equalsIgnoreCase("getWorker")) {
                try {
                    return (Worker) method.invoke(JavaPlugin.getPlugin(pluginClass));
                } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    @Nullable
    public static Worker getCoreWorker() {
        return getWorker(MommonsLoader.class);
    }

    @Nullable
    public static <T> T getCoreService(Class<T> clazz) {
        Worker coreWorker = getCoreWorker();
        return (T) coreWorker.getWorkerClasses().get(clazz).getObject();
    }

    @Nullable
    public static <T> T getService(Class<? extends WorkerPlugin> pluginClass, Class<T> clazz) {
        Worker worker = getWorker(pluginClass);
        if (worker == null) return null;
        return (T) worker.getWorkerClasses().get(clazz).getObject();
    }

    public static FileConfiguration getCoreConfiguration() {
        return MommonsLoader.getPlugin().getConfig();
    }
}
