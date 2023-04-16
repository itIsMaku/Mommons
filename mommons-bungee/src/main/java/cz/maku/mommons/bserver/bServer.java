package cz.maku.mommons.bserver;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static cz.maku.mommons.Mommons.GSON;

public class bServer {

    @NotNull
    private final String id;

    public bServer(@NotNull String id) {
        this.id = id;
    }

    @NotNull
    public Map<String, Object> getCloudData() {
        List<SQLRow> rows = MySQL.getApi().query("mommons_servers", "SELECT data FROM {table} WHERE id = ?", id);
        if (rows.isEmpty()) {
            return Maps.newHashMap();
        }
        SQLRow row = rows.get(0);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return GSON.fromJson(row.getString("data"), type);
    }

    @Nullable
    public Object getCloudValue(String key) {
        Map<String, Object> cloudData = getCloudData();
        return cloudData.get(key);
    }

    public int getPlayers() {
        Object cloudValue = getCloudValue("online-players");
        if (cloudValue == null) return 0;
        return (int) (double) cloudValue;
    }

    public CompletableFuture<bLocalServerInfo> getServerInfo() {
        return CompletableFuture.supplyAsync(() -> {
            String ip = (String) getCloudValue("ip");
            Object rawPort = getCloudValue("port");
            int port;
            if (rawPort == null) {
                port = 0;
            } else {
                port = (int) rawPort;
            }
            return new bLocalServerInfo(ip, port);
        });
    }

    @Nullable
    public String getType() {
        return (String) getCloudValue("server-type");
    }
}