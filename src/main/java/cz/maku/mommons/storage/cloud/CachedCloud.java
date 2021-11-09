package cz.maku.mommons.storage.cloud;

import com.google.common.collect.Maps;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Async;
import cz.maku.mommons.worker.annotation.Repeat;
import cz.maku.mommons.worker.annotation.Service;
import cz.maku.mommons.worker.annotation.sql.Download;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service(sql = true)
public class CachedCloud {

    @Getter(AccessLevel.PROTECTED)
    private final Map<String, String> cache = Maps.newConcurrentMap();

    public void set(String key, @Nullable String value) {
        if (cache.containsKey(key)) {
            MySQL.getApi().queryAsync("mommons_cachedcloud_data", "UPDATE {table} SET data_value = ? WHERE data_key = ?;", value, key);
        } else {
            MySQL.getApi().queryAsync("mommons_cachedcloud_data", "INSERT INTO {table} (data_key, data_value) VALUES (?, ?);", key, value);
        }
        cache.put(key, value);
    }

    @Nullable
    public String get(String key) {
        return cache.get(key);
    }

    @Download(table = "mommons_cachedcloud_data", query = "SELECT * FROM {table};", period = 1200L)
    @Async
    public void cacheWorker(CompletableFuture<List<SQLRow>> future) {
        future.thenAcceptAsync(rows -> {
            cache.clear();
            for (SQLRow row : rows) {
                cache.put(row.getString("data_key"), row.getString("data_value"));
            }
        });
    }

}
