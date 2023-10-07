package cz.maku.mommons.worker.plugin;

import com.google.common.collect.Lists;
import cz.maku.mommons.logger.LoggerHandler;
import cz.maku.mommons.plugin.MommonsPlugin;
import cz.maku.mommons.worker.BukkitWorker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class WorkerPlugin extends JavaPlugin {

    @Getter
    @Setter
    private BukkitWorker worker;

    @Nullable
    public abstract List<Class<?>> registerServices();

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public List<Class<?>> registerSpecialServices() {
        return Lists.newArrayList();
    }

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Thread thread = Thread.currentThread();
        if (thread.getName().equalsIgnoreCase("Server thread")) {
            thread.setName("main");
        }
        getLogger().addHandler(new LoggerHandler(getClass()));
        preWorkerLoad();
        worker = MommonsPlugin.getPlugin().getWorker();
        preLoad();
        worker.setJavaPlugin(this);
        List<Class<?>> classes = registerServices();
        if (classes == null) {
            classes = Lists.newArrayList();
        }
        worker.registerServices(classes.toArray(new Class<?>[0]));
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
