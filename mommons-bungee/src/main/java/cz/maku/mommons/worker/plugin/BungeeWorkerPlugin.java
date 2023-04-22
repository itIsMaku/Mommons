package cz.maku.mommons.worker.plugin;

import com.google.common.collect.Lists;
import cz.maku.mommons.logger.LoggerHandler;
import cz.maku.mommons.plugin.MommonsPluginBungee;
import cz.maku.mommons.worker.BungeeWorker;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public abstract class BungeeWorkerPlugin extends Plugin {

    @Getter
    @Setter
    private BungeeWorker worker;

    public abstract List<Class<?>> registerServices();

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public List<Class<?>> registerSpecialServices() {
        return Lists.newArrayList();
    }

    @Override
    public void onEnable() {
        getLogger().addHandler(new LoggerHandler(getClass()));
        preWorkerLoad();
        worker = MommonsPluginBungee.getPlugin().getWorker();
        preLoad();
        worker.setPlugin(this);
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
