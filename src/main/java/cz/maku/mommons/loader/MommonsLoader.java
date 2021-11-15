package cz.maku.mommons.loader;

import com.google.common.collect.Lists;
import cz.maku.mommons.storage.cloud.CachedCloud;
import cz.maku.mommons.storage.cloud.PlayerCloud;
import cz.maku.mommons.storage.database.SQLTable;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.Worker;
import cz.maku.mommons.worker.annotation.Plugin;
import cz.maku.mommons.worker.plugin.WorkerPlugin;
import lombok.Getter;

import java.util.List;

@Plugin(
        name = "MommonsLoader",
        main = "cz.maku.mommons.loader.MommonsLoader",
        authors = {"itIsMaku"},
        description = "Plugin what loads every plugin depends on Mommons",
        version = "1.1",
        apiVersion = "1.17"
)
public class MommonsLoader extends WorkerPlugin {

    @Getter
    private static MommonsLoader plugin;

    @Override
    public List<Class<?>> registerServices() {
        return Lists.newArrayList(CachedCloud.class, PlayerCloud.class);
    }

    @Override
    public List<Class<?>> registerSpecialServices() {
        return Lists.newArrayList();
    }

    public <T> T getConfigValue(Class<T> clazz, String path) {
        return getConfig().getObject(path, clazz);
    }

    @Override
    public void preWorkerLoad() {
        plugin = this;
    }

    @Override
    public void preLoad() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        setWorker(new Worker());
        getWorker().setPublicMySQL(new MySQL(
                getConfigValue(String.class, "address"),
                getConfigValue(Integer.class, "port"),
                getConfigValue(String.class, "database"),
                getConfigValue(String.class, "username"),
                getConfigValue(String.class, "password"),
                getConfigValue(Boolean.class, "ssl"),
                getConfigValue(Boolean.class, "auto-reconnect")
        ));
        MySQL.getApi().connect();

        SQLTable cachedCloud = new SQLTable("mommons_cachedcloud_data", MySQL.getApi());
        cachedCloud.addColumn("data_key", String.class);
        cachedCloud.addColumn("data_value", String.class);
        cachedCloud.create();

        SQLTable playerCloud = new SQLTable("mommons_playercloud_data", MySQL.getApi());
        playerCloud.addColumn("player", String.class);
        playerCloud.addColumn("data_key", String.class);
        playerCloud.addColumn("data_value", String.class);
        playerCloud.create();

    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onUnload() {
        MySQL.getApi().disconnect();
    }
}
