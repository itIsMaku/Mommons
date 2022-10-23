package cz.maku.mommons.player;

import com.google.common.collect.Maps;
import cz.maku.mommons.Response;
import cz.maku.mommons.player.event.CloudPlayerLoadEvent;
import cz.maku.mommons.player.event.CloudPlayerPreUnloadEvent;
import cz.maku.mommons.player.event.CloudPlayerUnloadEvent;
import cz.maku.mommons.server.ServerDataService;
import cz.maku.mommons.cloud.DirectCloud;
import cz.maku.mommons.cloud.DirectCloudStorage;
import cz.maku.mommons.worker.annotation.BukkitEvent;
import cz.maku.mommons.worker.annotation.Initialize;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

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
            initializePlayerAsync(player.getName());
        }
    }

    @BukkitEvent(AsyncPlayerPreLoginEvent.class)
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        System.out.println("preLogin");
        if (e.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            initializePlayerAsync(e.getName());
        }
    }

    @BukkitEvent(PlayerJoinEvent.class)
    public void onJoin(PlayerJoinEvent e) {
        System.out.println("join");
        Player player = e.getPlayer();
        load(player);
    }

    private void load(Player player) {
        if (PLAYERS.get(player.getName()) != null) {
            PLAYERS.get(player.getName()).setBukkit(player);
            CloudPlayerLoadEvent cloudPlayerLoadEvent = new CloudPlayerLoadEvent(player, PLAYERS.get(player.getName()));
            if (!cloudPlayerLoadEvent.isCancelled()) {
                Bukkit.getPluginManager().callEvent(cloudPlayerLoadEvent);
            }
        } else {
            player.kickPlayer("§cChyba -> §7Nepodařilo se nalézt tvou instanci hráče.");
        }
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
        CompletableFuture.runAsync(() -> {
            Object rawData = directCloud.get(DirectCloudStorage.PLAYER, "id", nickname, "data");
            if (rawData == null) {
                Response response = directCloud.insert(DirectCloudStorage.PLAYER, "id", nickname, "data", GSON.toJson(Maps.newHashMap()));
                if (Response.isException(response) || !Response.isValid(response)) {
                    if (player != null) {
                        player.kickPlayer("§cChyba -> §7Nastala chyba pri odesilani pozadavku na databazi.");
                    }
                }
            }
        });
        return new CloudPlayer(Maps.newHashMap(), Maps.newHashMap(), nickname, player);
    }

    private void initializePlayerAsync(String name) {
        //CompletableFuture.runAsync(() -> {
        CloudPlayer cloudPlayer = downloadCloudPlayer(name);
        PLAYERS.put(name, cloudPlayer);
        cloudPlayer.setCloudValue("connected-server", serverDataService.getServer().getId());
        //});
    }
}
