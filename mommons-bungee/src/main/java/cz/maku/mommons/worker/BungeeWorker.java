package cz.maku.mommons.worker;

import com.google.common.collect.Maps;
import cz.maku.mommons.plugin.MommonsPlugin;
import cz.maku.mommons.utils.Texts;
import cz.maku.mommons.worker.annotation.Service;
import lombok.AccessLevel;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Map;

@Getter(AccessLevel.PROTECTED)
public class BungeeWorker extends Worker {

    private Plugin plugin;

    public BungeeWorker() {
        setLogger(MommonsPlugin.getPlugin().getLogger());
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void stop() {
        super.stop();
        plugin = null;
    }

    @Override
    protected boolean make(Class<?> clazz, Object service, Map<String, WorkerExecutable> methods, Map<String, WorkerField> fields) {
        WorkerServiceClass workerClass = new WorkerServiceClass(this, clazz.getAnnotation(Service.class), service, methods, fields, getLogger(), Maps.newConcurrentMap());
        WorkerBungeeServiceClass workerBungeeServiceClass = new WorkerBungeeServiceClass(this, workerClass, Maps.newConcurrentMap());
        workerBungeeServiceClass.initializeFields();
        workerBungeeServiceClass.initializeMethods();
        workerClasses.put(clazz, workerBungeeServiceClass);
        getLogger().info("Bungee Service '" + Texts.getShortedClassName(clazz) + "' was successfully initialized.");
        return true;
    }
}
