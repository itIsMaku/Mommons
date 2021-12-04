package cz.maku.mommons.server;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.cache.ExpiringMap;
import cz.maku.mommons.storage.cloud.DirectCloud;
import cz.maku.mommons.storage.cloud.DirectCloudStorage;
import cz.maku.mommons.worker.WorkerReceiver;
import cz.maku.mommons.worker.annotation.Initialize;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Service;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Type;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class ServerDataService {

    private final FileConfiguration coreConfiguration = WorkerReceiver.getCoreConfiguration();
    @Getter
    private Server server;
    @Getter
    private LocalServerInfo localServerInfo;
    @Getter
    private ExpiringMap<String, Server> localCachedServers;

    @Load
    private DirectCloud directCloud;

    @SuppressWarnings("all")
    @SneakyThrows
    @Initialize
    public void serverInit() {
        String id = coreConfiguration.getString("server.id");
        Object object = directCloud.get(DirectCloudStorage.SERVER, "id", id, "data");
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        server = new Server(id, directCloud.getGson().fromJson((String) object, type), Maps.newConcurrentMap());
        localServerInfo = new LocalServerInfo();
    }

    @Initialize
    public void servers() {
        localCachedServers = new ExpiringMap<>(10, ChronoUnit.SECONDS);
    }



}
