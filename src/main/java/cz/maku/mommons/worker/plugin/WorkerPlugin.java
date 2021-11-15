package cz.maku.mommons.worker.plugin;

import cz.maku.mommons.loader.MommonsLoader;
import cz.maku.mommons.worker.Worker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class WorkerPlugin extends JavaPlugin {

    @Getter
    @Setter
    private Worker worker;

    public abstract List<Class<?>> registerServices();

    public abstract List<Class<?>> registerSpecialServices();

    @Override
    public void onEnable() {
        preWorkerLoad();
        worker = MommonsLoader.getPlugin().getWorker();
        preLoad();
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

    public void preWorkerLoad() {}

    public void preLoad() {}

    public void preUnLoad() {}

    public abstract void onUnload();

}
