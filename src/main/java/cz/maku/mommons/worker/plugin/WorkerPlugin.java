package cz.maku.mommons.worker.plugin;

import cz.maku.mommons.worker.Worker;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class WorkerPlugin extends JavaPlugin {

    @Getter
    private Worker worker;

    public abstract List<Class<?>> registerServices();

    public abstract List<Class<?>> registerSpecialServices();

    @Override
    public void onEnable() {
        preLoad();
        worker = new Worker();
        worker.setJavaPlugin(this);
        worker.registerServices(registerServices().toArray(new Class[0]));
        worker.registerSpecialServices(registerSpecialServices().toArray(new Class[0]));
        worker.initialize();
        onLoad();
    }

    @Override
    public void onDisable() {
        preUnLoad();
        worker.stop();
        onUnload();
    }

    public abstract void onLoad();

    public void preLoad() {}

    public void preUnLoad() {}

    public abstract void onUnload();

}
