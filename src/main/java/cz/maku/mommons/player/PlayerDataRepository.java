package cz.maku.mommons.player;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import cz.maku.mommons.Response;
import cz.maku.mommons.player.event.CloudPlayerLoadEvent;
import cz.maku.mommons.server.ServerDataService;
import cz.maku.mommons.storage.cloud.DirectCloud;
import cz.maku.mommons.storage.cloud.DirectCloudStorage;
import cz.maku.mommons.worker.annotation.BukkitEvent;
import cz.maku.mommons.worker.annotation.Initialize;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static cz.maku.mommons.Mommons.GSON;

@Service(listener = true)
public class PlayerDataRepository {

    public static final Map<String, CloudPlayer> PLAYERS = Maps.newConcurrentMap();

    @Load
    private DirectCloud directCloud;
    @Load
    private ServerDataService serverDataService;

    @Initialize
    public void initialize() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (PLAYERS.containsKey(player.getName())) continue;
            initializePlayerAsync(player);
        }
    }

    @BukkitEvent(PlayerJoinEvent.class)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        initializePlayerAsync(player);
    }

    @BukkitEvent(PlayerQuitEvent.class)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        CloudPlayer cloudPlayer = CloudPlayer.getInstance(player);
        if (cloudPlayer == null) return;
        CloudPlayerPreUnloadEvent cloudPlayerPreUnLoadEvent = new CloudPlayerPreUnloadEvent(player, cloudPlayer);
        Bukkit.getPluginManager().callEvent(cloudPlayerPreUnLoadEvent);
        PLAYERS.remove(player.getName());
        CloudPlayerUnloadEvent cloudPlayerUnloadEvent = new CloudPlayerUnloadEvent(player);
        Bukkit.getPluginManager().callEvent(cloudPlayerUnloadEvent);
    }

    @Nullable
    public CloudPlayer downloadCloudPlayer(String nickname) {
        Player player = Bukkit.getPlayer(nickname);
        Object rawData = directCloud.get(DirectCloudStorage.PLAYER, "id", nickname, "data");
        if (rawData == null) {
            Response response = directCloud.insert(DirectCloudStorage.PLAYER, "id", nickname, "data", GSON.toJson(Maps.newHashMap()));
            if (Response.isException(response) || !Response.isValid(response)) {
                if (player != null) {
                    player.kickPlayer("§cChyba -> §7Nastala chyba pri odesilani pozadavku na databazi.");
                    return null;
                }
            }
            rawData = GSON.toJson(Maps.newHashMap());
        }
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> data = GSON.fromJson((String) rawData, type);
        return new CloudPlayer(player, data, Maps.newHashMap());
    }

    public CompletableFuture<@Nullable CloudPlayer> downloadCloudPlayerAsync(String nickname) {
        return CompletableFuture.supplyAsync(() -> downloadCloudPlayer(nickname));
    }

    private void initializePlayerAsync(Player player) {
        downloadCloudPlayerAsync(player.getName()).thenAccept(cloudPlayer -> {
            PLAYERS.put(player.getName(), cloudPlayer);
            CloudPlayerLoadEvent cloudPlayerLoadEvent = new CloudPlayerLoadEvent(player, cloudPlayer);
            if (cloudPlayer == null) {
                cloudPlayerLoadEvent.setCancelled(true);
            }
            if (!cloudPlayerLoadEvent.isCancelled()) {
                Bukkit.getPluginManager().callEvent(cloudPlayerLoadEvent);
            }
            if (cloudPlayer != null) {
                cloudPlayer.setCloudValue("connected-server", serverDataService.getServer().getId());
            }
        });
    }
}
