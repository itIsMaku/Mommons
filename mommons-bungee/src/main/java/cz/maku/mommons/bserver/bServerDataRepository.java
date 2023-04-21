package cz.maku.mommons.bserver;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Async;
import cz.maku.mommons.worker.annotation.Repeat;
import cz.maku.mommons.worker.annotation.Service;
import cz.maku.mommons.worker.annotation.sql.Download;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static cz.maku.mommons.Mommons.GSON;

@Service(scheduled = true)
public class bServerDataRepository {

    public static final Map<String, bServer> SERVERS = Maps.newConcurrentMap();

    @Repeat(delay = 1, period = 5)
    @Async
    public void worker() {
        List<SQLRow> rows = MySQL.getApi().query("mommons_servers", "SELECT * FROM {table}");
        Map<String, bServer> newServers = Maps.newHashMap();
        for (SQLRow row : rows) {
            String id = row.getString("id");
            newServers.put(id,  new bServer(id));
        }
        SERVERS.entrySet().removeIf(e -> rows.stream().filter(row -> row.getString("id") != null).findFirst().orElse(null) == null);
        SERVERS.putAll(newServers);
    }
}
