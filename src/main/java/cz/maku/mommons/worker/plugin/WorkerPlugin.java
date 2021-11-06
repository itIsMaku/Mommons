package cz.maku.mommons.worker.plugin;

import cz.maku.mommons.worker.Worker;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WorkerPlugin extends JavaPlugin {

    private Worker worker;

    @Override
    public void onEnable() {
        preLoad();
        worker = new Worker();
        worker.setJavaPlugin(this);
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
