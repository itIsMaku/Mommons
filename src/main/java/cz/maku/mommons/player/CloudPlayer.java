package cz.maku.mommons.player;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Response;
import cz.maku.mommons.loader.MommonsLoader;
import cz.maku.mommons.server.Server;
import cz.maku.mommons.server.ServerData;
import cz.maku.mommons.server.ServerDataService;
import cz.maku.mommons.storage.cloud.CloudData;
import cz.maku.mommons.storage.cloud.DirectCloud;
import cz.maku.mommons.storage.cloud.DirectCloudStorage;
import cz.maku.mommons.storage.local.LocalData;
import cz.maku.mommons.worker.Worker;
import cz.maku.mommons.worker.WorkerReceiver;
import lombok.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static cz.maku.mommons.Mommons.GSON;

@AllArgsConstructor
public class CloudPlayer implements CloudData, LocalData {

    @NotNull
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String nickname;
    private Player player;
    @NotNull
    @Getter
    private final Map<String, Object> cachedData;
    @NotNull
    @Getter
    private final Map<String, Object> localData;

    @Nullable
    public static CloudPlayer getInstance(String nickname) {
        return PlayerDataRepository.PLAYERS.get(nickname);
    }

    @Nullable
    public static CloudPlayer getInstance(Player player) {
        return getInstance(player.getName());
    }

    @Nullable
    public static CloudPlayer getInstanceOrDownload(String nickname) {
        CloudPlayer instance = getInstance(nickname);
        if (instance == null) {
            return WorkerReceiver.getCoreService(PlayerDataRepository.class).downloadCloudPlayer(nickname);
        }
        return instance;
    }

    @Nullable
    public static CloudPlayer getInstanceOrDownload(Player player) {
        return getInstanceOrDownload(player.getName());
    }

    public static CompletableFuture<@Nullable CloudPlayer> getInstanceOrDownloadAsync(String nickname) {
        return CompletableFuture.supplyAsync(() -> getInstanceOrDownload(nickname));
    }

    public static CompletableFuture<@Nullable CloudPlayer> getInstanceOrDownloadAsync(Player player) {
        return getInstanceOrDownloadAsync(player.getName());
    }

    @NotNull
    public Map<String, Object> getCloudData() {
        DirectCloud directCloud = WorkerReceiver.getCoreService(DirectCloud.class);
        if (directCloud == null) {
            MommonsLoader.getPlugin().getLogger().warning("DirectCloud is null (service from core Worker).");
            return Maps.newHashMap();
        }
        Object object = directCloud.get(DirectCloudStorage.PLAYER, "id", nickname, "data");
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
    @NotNull
    public CompletableFuture<@NotNull Response> setCloudValue(String key, Object value) {
        WorkerReceiver.getCoreWorker().getLogger().warning(String.format("SetCloudValue for player %s: %s -> %s", nickname, key, value));
        DirectCloud directCloud = WorkerReceiver.getCoreService(DirectCloud.class);
        if (directCloud == null)
            return CompletableFuture.completedFuture(new Response(Response.Code.ERROR, "DirectCloud is null (service from core Worker)."));
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> data = getCloudData();
                if (data.containsKey(key) && value == null) {
                    data.remove(key);
                    return directCloud.update(DirectCloudStorage.PLAYER, "id", nickname, "data", GSON.toJson(data));
                }
                data.put(key, value);
                if (!data.containsKey(key)) {
                    return directCloud.insert(DirectCloudStorage.PLAYER, "id", nickname, "data", GSON.toJson(data));
                } else {
                    return directCloud.update(DirectCloudStorage.PLAYER, "id", nickname, "data", GSON.toJson(data));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionResponse(Response.Code.ERROR, "Exception while setting data.", e);
            }
        });
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

    public Player bukkit() {
        return player;
    }

    protected void setBukkit(Player player) {
        this.player = player;
    }

    @Nullable
    public Server getConnectedServer() {
        Object raw = getCloudValue("connected-server");
        if (raw == null) return null;
        return ServerData.getServer((String) raw);
    }
}