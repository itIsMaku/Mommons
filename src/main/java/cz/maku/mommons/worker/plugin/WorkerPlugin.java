package cz.maku.mommons.worker.plugin;

import cz.maku.mommons.loader.MommonsLoader;
import cz.maku.mommons.utils.Texts;
import cz.maku.mommons.worker.Worker;
import cz.maku.mommons.worker.type.ConsoleColors;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public abstract class WorkerPlugin extends JavaPlugin {

    @Getter
    @Setter
    private Worker worker;

    public abstract List<Class<?>> registerServices();

    public abstract List<Class<?>> registerSpecialServices();

    @Override
    public void onEnable() {
        if (Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
            Thread.currentThread().setName("main");
        }
        getLogger().addHandler(new Handler() {
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

    public void preWorkerLoad() {
    }

    public void preLoad() {
    }

    public void preUnLoad() {
    }

    public abstract void onUnload();
}
