package cz.maku.mommons.player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.maku.mommons.Mommons;
import cz.maku.mommons.Response;
import cz.maku.mommons.data.MySQLSavableData;
import cz.maku.mommons.plugin.MommonsPlugin;
import cz.maku.mommons.server.Server;
import cz.maku.mommons.server.ServerData;
import cz.maku.mommons.storage.local.LocalData;
import cz.maku.mommons.worker.WorkerReceiver;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CloudPlayer extends MySQLSavableData implements LocalData {

    @NotNull
    @Getter
    private final Map<String, Object> cachedData;
    @NotNull
    @Getter
    private final Map<String, Object> localData;
    @NotNull
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String nickname;
    private Player player;

    public CloudPlayer(CloudPlayer cloudPlayer) {
        this(cloudPlayer.getCachedData(), cloudPlayer.getLocalData(), cloudPlayer.getNickname(), cloudPlayer.bukkit());
    }

    public CloudPlayer(@NotNull Map<String, Object> cachedData, @NotNull Map<String, Object> localData, @NotNull String nickname, Player player) {
        super("mommons_players", "id", "data", nickname);
        this.cachedData = cachedData;
        this.localData = localData;
        this.nickname = nickname;
        this.player = player;
    }

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
            PlayerDataRepository playerDataRepository = WorkerReceiver.getCoreService(PlayerDataRepository.class);
            if (playerDataRepository == null) return null;
            return playerDataRepository.downloadCloudPlayer(nickname);
        }
        return instance;
    }

    @Nullable
    public static CloudPlayer getInstanceOrDownload(Player player) {
        return getInstanceOrDownload(player.getName());
    }

    public static CompletableFuture<@Nullable CloudPlayer> getInstanceOrDownloadAsync(String nickname) {
        return CompletableFuture.supplyAsync(() -> getInstanceOrDownload(nickname), Mommons.ES);
    }

    public static CompletableFuture<@Nullable CloudPlayer> getInstanceOrDownloadAsync(Player player) {
        return getInstanceOrDownloadAsync(player.getName());
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

    @NotNull
    @Deprecated
    public CompletableFuture<@NotNull Response> setCloudValue(String key, Object value) {
        return setValueAsync(key, value, true);
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
        Object raw = getValue("connected-server");
        if (raw == null) return null;
        return ServerData.getServer((String) raw);
    }

    public void connect(String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(MommonsPlugin.getPlugin(), "BungeeCord", out.toByteArray());
    }

    public void connect(Server server) {
        connect(server.getId());
    }
}