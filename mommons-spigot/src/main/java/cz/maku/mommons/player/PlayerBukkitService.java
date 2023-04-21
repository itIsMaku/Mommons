package cz.maku.mommons.player;

import cz.maku.mommons.player.event.CloudPlayerPreUnloadEvent;
import cz.maku.mommons.player.event.CloudPlayerUnloadEvent;
import cz.maku.mommons.worker.annotation.BukkitEvent;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Service(listener = true)
public class PlayerBukkitService {

    @Load
    private PlayerDataRepository playerDataRepository;

    @BukkitEvent(AsyncPlayerPreLoginEvent.class)
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        if (e.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            playerDataRepository.initializePlayerAsync(e.getName());
        }
    }

    @BukkitEvent(PlayerJoinEvent.class)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        playerDataRepository.load(player);
    }

    @BukkitEvent(PlayerQuitEvent.class)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        CloudPlayer cloudPlayer = CloudPlayer.getInstance(player);
        if (cloudPlayer == null) return;
        CloudPlayerPreUnloadEvent cloudPlayerPreUnLoadEvent = new CloudPlayerPreUnloadEvent(player, cloudPlayer);
        Bukkit.getPluginManager().callEvent(cloudPlayerPreUnLoadEvent);
        PlayerDataRepository.PLAYERS.remove(player.getName());
        CloudPlayerUnloadEvent cloudPlayerUnloadEvent = new CloudPlayerUnloadEvent(player);
        Bukkit.getPluginManager().callEvent(cloudPlayerUnloadEvent);
    }

}
