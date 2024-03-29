package cz.maku.mommons.plugin;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import cz.maku.mommons.bserver.bServerDataRepository;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.BungeeWorker;
import cz.maku.mommons.worker.plugin.BungeeWorkerPlugin;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class MommonsPluginBungee extends BungeeWorkerPlugin {

    @Getter
    private static MommonsPluginBungee plugin;

    @Getter
    private Configuration configuration;

    @Override
    public List<Class<?>> registerServices() {
        return Lists.newArrayList(bServerDataRepository.class);
    }

    @Override
    public void preWorkerLoad() {
        plugin = this;
    }

    @SneakyThrows
    @Override
    public void preLoad() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            FileOutputStream outputStream = new FileOutputStream(configFile);
            InputStream inputStream = getResourceAsStream("config.yml");
            ByteStreams.copy(inputStream, outputStream);
        }
        configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        setWorker(new BungeeWorker());
        getWorker().setPublicMySQL(new MySQL(
                configuration.getString("address"),
                configuration.getInt("port"),
                configuration.getString("database"),
                configuration.getString("username"),
                configuration.getString("password"),
                configuration.getBoolean("ssl"),
                configuration.getBoolean("auto-reconnect")
        ));
        MySQL.getApi().connect();
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
        MySQL.getApi().disconnect();
    }
}
