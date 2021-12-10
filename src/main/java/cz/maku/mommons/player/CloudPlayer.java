package cz.maku.mommons.player;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Response;
import cz.maku.mommons.storage.cloud.CloudData;
import cz.maku.mommons.storage.cloud.DirectCloud;
import cz.maku.mommons.storage.cloud.DirectCloudStorage;
import cz.maku.mommons.worker.WorkerReceiver;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static cz.maku.mommons.Mommons.GSON;

@AllArgsConstructor
public class CloudPlayer implements CloudData {

    @NotNull
    private final Player player;
    @NotNull
    private final Map<String, Object> cachedData;
    @NotNull
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
        return CompletableFuture.supplyAsync(() -> getInstanceOrDownload(player.getName()));
    }

    @NotNull
    public Map<String, Object> getCloudData() {
        DirectCloud directCloud = WorkerReceiver.getCoreService(DirectCloud.class);
        if (directCloud == null) {
            LoggerFactory.getLogger(CloudPlayer.class).warn("DirectCloud is null (service from core Worker).");
            return Maps.newHashMap();
        }
        Object object = directCloud.get(DirectCloudStorage.SERVER, "id", player.getName(), "data");
        if (object == null) return Maps.newHashMap();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return directCloud.getGson().fromJson((String) object, type);
    }

    @Override
    public Object getCloudValue(String key) {
        Map<String, Object> cloudData = getCloudData();
        return cloudData.get(key);
    }

    @Override
    public CompletableFuture<Response> setCloudValue(String key, Object value) {
        DirectCloud directCloud = WorkerReceiver.getCoreService(DirectCloud.class);
        if (directCloud == null)
            return CompletableFuture.completedFuture(new Response(Response.Code.ERROR, "DirectCloud is null (service from core Worker)."));
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> data = getCloudData();
                if (data.containsKey(key) && value == null) {
                    data.remove(key);
                    return directCloud.update(DirectCloudStorage.SERVER, "id", player.getName(), "data", GSON.toJson(data));
                }
                data.put(key, value);
                if (!data.containsKey(key)) {
                    return directCloud.insert(DirectCloudStorage.SERVER, "id", player.getName(), "data", GSON.toJson(data));
                } else {
                    return directCloud.update(DirectCloudStorage.SERVER, "id", player.getName(), "data", GSON.toJson(data));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionResponse(Response.Code.ERROR, "Exception while setting data.", e);
            }
        });
    }
}