package cz.maku.mommons.server;

import com.google.gson.reflect.TypeToken;
import cz.maku.mommons.cache.ExpiringMap;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.worker.annotation.Async;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Service;
import cz.maku.mommons.worker.annotation.sql.Download;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static cz.maku.mommons.Mommons.GSON;

@Service(sql = true, scheduled = true)
public class ServerDataWorker {

    @Load
    private ServerDataService serverDataService;

    @Download(table = "mommons_servers", query = "SELECT * FROM {table};", period = 5)
    @Async
    public void worker(List<SQLRow> rows) {
        ExpiringMap<String, Server> localCachedServers = serverDataService.getLocalCachedServers();
        localCachedServers.clear();
        for (SQLRow row : rows) {
            String id = row.getString("id");
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> data = GSON.fromJson(row.getString("data"), type);
            localCachedServers.renew(id, new Server(id));
        }
    }

}
