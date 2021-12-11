package cz.maku.mommons.server;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Response;
import cz.maku.mommons.loader.MommonsLoader;
import cz.maku.mommons.storage.cloud.CloudData;
import cz.maku.mommons.storage.cloud.DirectCloud;
import cz.maku.mommons.storage.cloud.DirectCloudStorage;
import cz.maku.mommons.worker.WorkerReceiver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static cz.maku.mommons.Mommons.GSON;

@RequiredArgsConstructor
@Getter
public class Server implements CloudData {

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

    @Nullable
    public Object getLocalValue(String key) {
        return localData.get(key);
    }

    public void setLocalValue(String key, Object value) {
        if (localData.containsKey(key) && value == null) {
            localData.remove(key);
            return;
        }
        localData.put(key, value);
    }

    @NotNull
    public Map<String, Object> getCloudData() {
        DirectCloud directCloud = WorkerReceiver.getCoreService(DirectCloud.class);
        if (directCloud == null) {
            MommonsLoader.getPlugin().getLogger().warning("DirectCloud is null (service from core Worker).");
            return Maps.newHashMap();
        }
        Object object = directCloud.get(DirectCloudStorage.SERVER, "id", id, "data");
        if (object == null) return Maps.newHashMap();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return directCloud.getGson().fromJson((String) object, type);
    }

    @Override
    @Nullable
    public Object getCloudValue(String key) {
        Map<String, Object> cloudData = getCloudData();
        return cloudData.get(key);
    }

    @Override
    @Nullable
    public CompletableFuture<Response> setCloudValue(String key, Object value) {
        DirectCloud directCloud = WorkerReceiver.getCoreService(DirectCloud.class);
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

}
