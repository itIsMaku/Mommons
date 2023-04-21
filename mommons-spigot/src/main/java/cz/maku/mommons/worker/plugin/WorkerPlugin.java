package cz.maku.mommons.worker.plugin;

import com.google.common.collect.Lists;
import cz.maku.mommons.logger.LoggerHandler;
import cz.maku.mommons.plugin.MommonsPlugin;
import cz.maku.mommons.worker.BukkitWorker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public abstract class WorkerPlugin extends JavaPlugin {

    @Getter
    @Setter
    private BukkitWorker worker;

    public abstract List<Class<?>> registerServices();

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public List<Class<?>> registerSpecialServices() {
        return Lists.newArrayList();
    }

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        if (Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
            Thread.currentThread().setName("main");
        }
        getLogger().addHandler(new LoggerHandler(getClass()));
        preWorkerLoad();
        worker = MommonsPlugin.getPlugin().getWorker();
        preLoad();
        worker.setJavaPlugin(this);
        worker.registerServices(registerServices().toArray(new Class[0]));
        worker.initialize();
        load();
    }

    @Override
    public void onDisable() {
        preUnLoad();
        worker.stop();
        unload();
    }

    public abstract void load();

    public void preWorkerLoad() {
    }

    public void preLoad() {
    }

    public void preUnLoad() {
    }

    public abstract void unload();
}
