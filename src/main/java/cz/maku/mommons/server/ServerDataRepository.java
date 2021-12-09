package cz.maku.mommons.server;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.worker.annotation.Async;
import cz.maku.mommons.worker.annotation.Service;
import cz.maku.mommons.worker.annotation.sql.Download;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static cz.maku.mommons.Mommons.GSON;

@Service(scheduled = true, sql = true)
public class ServerDataRepository {

    public static final Map<String, Server> SERVERS = Maps.newConcurrentMap();

    @Download(table = "mommons_servers", query = "SELECT * FROM {table};", period = 5)
    @Async
    public void worker(List<SQLRow> rows) {
        Map<String, Server> newServers = Maps.newHashMap();
        for (SQLRow row : rows) {
            String id = row.getString("id");
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> data = GSON.fromJson(row.getString("data"), type);
            if (SERVERS.containsKey(id)) {
                Server server = SERVERS.get(id);
                server.getCloudData().clear();
                server.getCloudData().putAll(data);
                continue;
            }
            Server server = new Server(id, data, Maps.newHashMap());
            server.getCloudData().putAll(data);
            newServers.put(id, server);
        }
        SERVERS.entrySet().removeIf(e -> rows.stream().filter(row -> row.getString("id") != null).findFirst().orElse(null) == null);
        SERVERS.putAll(newServers);
    }

}
