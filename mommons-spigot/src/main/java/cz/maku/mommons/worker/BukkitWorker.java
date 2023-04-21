package cz.maku.mommons.worker;

import com.google.common.collect.Maps;
import cz.maku.mommons.logger.LoggerHandler;
import cz.maku.mommons.plugin.MommonsPlugin;
import cz.maku.mommons.utils.Texts;
import cz.maku.mommons.worker.annotation.Service;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.logging.Logger;

@Getter(AccessLevel.PROTECTED)
public class BukkitWorker extends Worker {

    private JavaPlugin javaPlugin;

    public BukkitWorker() {
        Logger logger = Logger.getLogger("Worker");
        logger.addHandler(new LoggerHandler(getClass()));
        setLogger(logger);
    }

    public void setJavaPlugin(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    @Override
    public void stop() {
        super.stop();
        javaPlugin = null;
    }

    @Override
    protected boolean make(Class<?> clazz, Object service, Map<String, WorkerExecutable> methods, Map<String, WorkerField> fields) {
        WorkerServiceClass workerClass = new WorkerServiceClass(this, clazz.getAnnotation(Service.class), service, methods, fields, getLogger(), Maps.newConcurrentMap());
        WorkerBukkitServiceClass workerBukkitServiceClass = new WorkerBukkitServiceClass(this, workerClass, Maps.newConcurrentMap());
        workerBukkitServiceClass.initializeFields();
        workerBukkitServiceClass.initializeMethods();
        workerClasses.put(clazz, workerBukkitServiceClass);
        getLogger().info("Bukkit Service '" + Texts.getShortedClassName(clazz) + "' was successfully initialized.");
        return true;
    }
}
