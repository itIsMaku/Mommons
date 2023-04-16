package cz.maku.mommons.server;

import com.google.common.reflect.TypeToken;
import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Response;
import cz.maku.mommons.cloud.CloudData;
import cz.maku.mommons.cloud.DirectCloud;
import cz.maku.mommons.cloud.DirectCloudStorage;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.storage.local.LocalData;
import cz.maku.mommons.worker.WorkerReceiver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static cz.maku.mommons.Mommons.GSON;

@RequiredArgsConstructor
@Getter
public class Server implements CloudData, LocalData {

    @NotNull
    private final String id;
    @NotNull
    private final Map<String, Object> cachedData;
    @NotNull
    private final Map<String, Object> localData;

    @SuppressWarnings("all")
    @NotNull
    public static Server local() {
        return WorkerReceiver.getCoreService(ServerDataService.class).getServer();
    }

    @NotNull
    public Map<String, Object> getCloudData() {
        /*DirectCloud directCloud = WorkerReceiver.getCoreService(DirectCloud.class);
        if (directCloud == null) {
            MommonsPlugin.getPlugin().getLogger().warning("DirectCloud is null (service from core Worker).");
            return Maps.newHashMap();
        }
        Object object = directCloud.get(DirectCloudStorage.SERVER, "id", id, "data");
        if (object == null) return Maps.newHashMap();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return directCloud.getGson().fromJson((String) object, type);*/
        List<SQLRow> rows = MySQL.getApi().query(DirectCloudStorage.SERVER.getSqlTable(), "SELECT data FROM {table} WHERE id = ?", id);
        if (rows.isEmpty()) {
            throw new RuntimeException("No data found for server " + id);
        }
        SQLRow row = rows.get(0);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return GSON.fromJson(row.getString("data"), type);
    }

    @Override
    @Nullable
    public Object getCloudValue(String key) {
        Map<String, Object> cloudData = getCloudData();
        return cloudData.get(key);
    }

    @Override
    public CompletableFuture<Response> setCloudValue(String key, Object value) {
        /*DirectCloud directCloud = WorkerReceiver.getCoreService(DirectCloud.class);
        if (directCloud == null)
            return CompletableFuture.completedFuture(new Response(Response.Code.ERROR, "DirectCloud is null (service from core Worker)."));
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> data = getCloudData();
                if (data.containsKey(key) && value == null) {
                    data.remove(key);
                    return directCloud.update(DirectCloudStorage.SERVER, "id", id, "data", GSON.toJson(data));
                }
                data.put(key, value);
                if (!data.containsKey(key)) {
                    return directCloud.insert(DirectCloudStorage.SERVER, "id", id, "data", GSON.toJson(data));
                } else {
                    return directCloud.update(DirectCloudStorage.SERVER, "id", id, "data", GSON.toJson(data));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionResponse(Response.Code.ERROR, "Exception while setting data.", e);
            }
        });*/
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> cloudData = getCloudData();
                if (cloudData.containsKey(key) && value == null) {
                    cloudData.remove(key);
                } else {
                    cloudData.put(key, value);
                }
                MySQL.getApi().query(DirectCloudStorage.SERVER.getSqlTable(), "UPDATE {table} SET data = ? WHERE id = ?", GSON.toJson(cloudData), id);
                return new Response(Response.Code.SUCCESS, "Successfully set data.");
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionResponse(Response.Code.ERROR, "Exception while setting data.", e);
            }
        });
    }

    public CompletableFuture<Response> setMultipleCloudValues(Map<String, Object> data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> cloudData = getCloudData();
                cloudData.putAll(data);
                MySQL.getApi().query(DirectCloudStorage.SERVER.getSqlTable(), "UPDATE {table} SET data = ? WHERE id = ?", GSON.toJson(cloudData), id);
                return new Response(Response.Code.SUCCESS, "Successfully set data.");
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionResponse(Response.Code.ERROR, "Exception while setting data.", e);
            }
        });
    }

    public int getPlayers() {
        Object cloudValue = getCloudValue("online-players");
        if (cloudValue == null) return 0;
        return (int) cloudValue;
    }

    public CompletableFuture<Response> setPlayers(int players) {
        return setCloudValue("online-players", players);
    }

    public CompletableFuture<LocalServerInfo> getServerInfo() {
        return CompletableFuture.supplyAsync(() -> {
            String ip = (String) getCloudValue("ip");
            Object rawPort = getCloudValue("port");
            int port;
            if (rawPort == null) {
                port = 0;
            } else {
                port = (int) rawPort;
            }
            return new LocalServerInfo(ip, port);
        });
    }

    @NotNull
    public String getType() {
        return (String) Objects.requireNonNull(getCloudValue("server-type"));
    }

    public CompletableFuture<Response> setType(String type) {
        return setCloudValue("server-type", type);
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