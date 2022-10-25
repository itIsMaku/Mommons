package cz.maku.mommons.worker.plugin;

import cz.maku.mommons.plugin.MommonsPlugin;
import cz.maku.mommons.utils.ConsoleColors;
import cz.maku.mommons.utils.Texts;
import cz.maku.mommons.worker.BungeeWorker;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collection;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public abstract class WorkerPlugin extends Plugin {

    @Getter
    @Setter
    private BungeeWorker worker;

    public abstract List<Class<?>> registerServices();

    public abstract List<Class<?>> registerSpecialServices();

    @Override
    public void onEnable() {
        Collection<Plugin> plugins = getProxy().getPluginManager().getPlugins();
        for (Plugin plugin : plugins) {
            createLoggerHandler(plugin.getLogger());
        }
        createLoggerHandler(getLogger());
        preWorkerLoad();
        worker = MommonsPlugin.getPlugin().getWorker();
        preLoad();
        worker.setPlugin(this);
        worker.registerServices(registerServices().toArray(new Class[0]));
        worker.registerSpecialServices(registerSpecialServices().toArray(new Class[0]));
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

    public void createLoggerHandler(Logger logger) {
        logger.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                if (record.getLevel().equals(Level.INFO)) {
                    record.setLoggerName(ConsoleColors.GREEN_BRIGHT + Thread.currentThread().getName() + ConsoleColors.WHITE_BRIGHT);
                } else {
                    record.setLoggerName(Thread.currentThread().getName());
                }
                record.setMessage(Texts.getShortedClassName(record.getSourceClassName()) + " : " + record.getMessage());
            }

            @Override
            public void flush() {
                System.out.flush();
            }

            @Override
            public void close() throws SecurityException {
                System.out.close();
            }
        });
    }
}
