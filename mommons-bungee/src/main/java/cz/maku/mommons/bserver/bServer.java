package cz.maku.mommons.bserver;

import cz.maku.mommons.Mommons;
import cz.maku.mommons.data.MySQLSavableData;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class bServer extends MySQLSavableData {

    @NotNull
    @Getter
    private final String id;

    public bServer(@NotNull String id) {
        super("mommons_servers", "id", "data", id);
        this.id = id;

    }

    @NotNull
    @Deprecated
    public Map<String, Object> getCloudData() {
        return getValues();
    }

    @Nullable
    @Deprecated
    public Object getCloudValue(String key) {
        return getValue(key);
    }

    public int getPlayers() {
        Object value = getValue("online-players");
        if (value == null) return 0;
        return (int) (double) value;
    }

    public CompletableFuture<bLocalServerInfo> getServerInfo() {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> values = getValues();
            String ip = (String) values.get("ip");
            Object rawPort = values.get("port");
            int port;
            if (rawPort == null) {
                port = 0;
            } else {
                port = (int) rawPort;
            }
            return new bLocalServerInfo(ip, port);
        }, Mommons.ES);
    }

    @Nullable
    public String getType() {
        return (String) getValue("server-type");
    }
}