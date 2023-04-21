package cz.maku.mommons.server;

import cz.maku.mommons.Response;
import cz.maku.mommons.data.MySQLSavableData;
import cz.maku.mommons.storage.local.LocalData;
import cz.maku.mommons.worker.WorkerReceiver;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
public class Server extends MySQLSavableData implements LocalData {

    @NotNull
    private final String id;
    @NotNull
    private final Map<String, Object> cachedData;
    @NotNull
    private final Map<String, Object> localData;

    public Server(@NotNull String id, @NotNull Map<String, Object> cachedData, @NotNull Map<String, Object> localData) {
        super("mommons_servers", "id", "data", id);
        this.id = id;
        this.cachedData = cachedData;
        this.localData = localData;
    }

    @SuppressWarnings("all")
    @NotNull
    public static Server local() {
        return WorkerReceiver.getCoreService(ServerDataService.class).getServer();
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

    @Deprecated
    public CompletableFuture<Response> setCloudValue(String key, Object value) {
        return setValueAsync(key, value, true);
    }

    @Deprecated
    public CompletableFuture<Response> setMultipleCloudValues(Map<String, Object> data) {
        return setMultipleValuesAsync(data);
    }

    public int getPlayers() {
        Object value = getValue("online-players");
        if (value == null) return 0;
        return (int) (double) value;
    }

    public CompletableFuture<Response> setPlayers(int players) {
        return setValueAsync("online-players", players, true);
    }

    public CompletableFuture<LocalServerInfo> getServerInfo() {
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
            return new LocalServerInfo(ip, port);
        });
    }

    @Nullable
    public String getType() {
        return (String) getValue("server-type");
    }

    public CompletableFuture<Response> setType(String type) {
        return setValueAsync("server-type", type, true);
    }

    @Override
    @Nullable
    public Object getLocalValue(String key) {
        return localData.get(key);
    }

    @Override
    public Response setLocalValue(String key, Object value) {
        return setLocalValueWithResponse(key, value, localData);
    }
}