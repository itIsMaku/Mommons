package cz.maku.mommons.storage.cloud;

import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Service;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PlayerCloud {

    public void set(String key, String value, Player player) {
        if (MySQL.getApi().query("mommons_playercloud_data", "SELECT * FROM {table} WHERE data_key = ? AND player = ?;", key, player.getName()).isEmpty()) {
            MySQL.getApi().query("mommons_playercloud_data", "INSERT INTO {table} (data_key, data_value, player) VALUES (?, ?, ?);", key, value, player.getName());
        } else {
            MySQL.getApi().query("mommons_playercloud_data", "UPDATE {table} SET data_value = ? WHERE data_key = ? AND player = ?;", value, key, player.getName());
        }
    }

    public void setAsync(String key, String value, Player player) {
        CompletableFuture.runAsync(() -> set(key, value, player));
    }

    @Nullable
    public String get(String key, Player player) {
        List<SQLRow> rows = MySQL.getApi().query("mommons_playercloud_data", "SELECT * FROM {table} WHERE data_key = ? AND player = ?;", key, player.getName());
        if (rows.isEmpty()) return null;
        return rows.get(0).getString(key);
    }

}
