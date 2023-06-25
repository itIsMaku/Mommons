package cz.maku.mommons.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.plugin.MommonsPlugin;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.WorkerReceiver;
import cz.maku.mommons.worker.annotation.Destroy;
import cz.maku.mommons.worker.annotation.Initialize;
import cz.maku.mommons.worker.annotation.Service;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.Optional;

import static cz.maku.mommons.Mommons.GSON;


@SuppressWarnings("unused")
@Service
public class ServerDataService {

    private final FileConfiguration coreConfiguration = WorkerReceiver.getCoreConfiguration();
    @Getter
    private Server server;
    @Getter
    private LocalServerInfo localServerInfo;

    @SneakyThrows
    @Initialize
    public void serverInit() {
        LocalServerInfo.of().thenAccept(localServerInfo -> {
            this.localServerInfo = localServerInfo;
            String id = coreConfiguration.getString("server.id");
            if (id == null) {
                MommonsPlugin.getPlugin().getLogger().severe("Server id is null!");
                return;
            }
            Optional<SQLRow> optionalRow = MySQL.getApi().single("mommons_servers", "SELECT * FROM {table} WHERE id = ?;", id);
            Map<String, Object> data = Maps.newHashMap();
            data.put("server-info", GSON.toJson(localServerInfo));
            if (!optionalRow.isPresent()) {
                MySQL.getApi().query("mommons_servers", "INSERT INTO {table} (id, data) VALUES (?, ?);", id, GSON.toJson(data));
            }
            Map<String, Object> oldData = Maps.newHashMap();
            if (optionalRow.isPresent()) {
                oldData = optionalRow.get().getJsonObject("data", new TypeToken<Map<String, Object>>() {
                }.getType());
            }
            server = new Server(id, oldData, Maps.newConcurrentMap());
            server.setMultipleValues(
                    ImmutableMap.of(
                            "server-info", GSON.toJson(localServerInfo),
                            "server-type", "unknown"
                    )
            );
        });
    }

    @Destroy
    public void destroy() {
        MySQL.getApi().query("mommons_servers", "DELETE FROM {table} WHERE id = ?;", server.getId());
    }
}
