package cz.maku.mommons.storage.cloud;

import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Service;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PlayerCloud {

    public void set(String key, String value) {
        if (MySQL.getApi().query("mommons_playercloud_data", "SELECT * FROM {table} WHERE data_key = ?;", key).isEmpty()) {
            MySQL.getApi().query("mommons_playercloud_data", "INSERT INTO {table} (data_key, data_value) VALUES (?, ?);", key, value);
        } else {
            MySQL.getApi().query("mommons_playercloud_data", "UPDATE {table} SET data_value = ? WHERE data_key = ?;", value, key);
        }
    }

    public void setAsync(String key, String value) {
        CompletableFuture.runAsync(() -> set(key, value));
    }

    @Nullable
    public String get(String key) {
        List<SQLRow> rows = MySQL.getApi().query("mommons_playercloud_data", "SELECT * FROM {table} WHERE data_key = ?;", key);
        if (rows.isEmpty()) return null;
        return rows.get(0).getString(key);
    }

}
