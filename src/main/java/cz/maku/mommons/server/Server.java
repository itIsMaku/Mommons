package cz.maku.mommons.server;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Response;
import cz.maku.mommons.storage.cloud.CloudData;
import cz.maku.mommons.storage.cloud.DirectCloud;
import cz.maku.mommons.storage.cloud.DirectCloudStorage;
import cz.maku.mommons.utils.Pair;
import cz.maku.mommons.worker.WorkerLogger;
import cz.maku.mommons.worker.WorkerReceiver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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
    public static Server getServerById(String id) {
        return WorkerReceiver.getCoreService(ServerDataService.class).getLocalCachedServers().get(id);
    }

    public static Map<String, Server> getServersByCondition(BiFunction<String, Server, Boolean> function) {
        ServerDataService serverDataService = WorkerReceiver.getCoreService(ServerDataService.class);
        Map<String, Pair<Server, LocalDateTime>> data = serverDataService.getLocalCachedServers().getData();
        List<String> ids = data.keySet().stream().filter(id -> function.apply(id, data.get(id).getFirst())).collect(Collectors.toList());
        Map<String, Server> servers = new HashMap<>();
        for (String id : ids) {
            servers.put(id, getServerById(id));
        }
        return servers;
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
            WorkerLogger.error("DirectCloud is null (service from core Worker).");
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
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> data = getCloudData();
                if (data.containsKey(key) && value == null) {
                    data.remove(key);
                    return new Response(Response.Code.SUCCESS, null);
                }
                data.put(key, value);
                return new Response(Response.Code.SUCCESS, null);
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

}
