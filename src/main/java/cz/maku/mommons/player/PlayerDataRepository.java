package cz.maku.mommons.player;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import cz.maku.mommons.Response;
import cz.maku.mommons.storage.cloud.DirectCloud;
import cz.maku.mommons.storage.cloud.DirectCloudStorage;
import cz.maku.mommons.worker.annotation.BukkitEvent;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Type;
import java.util.Map;

import static cz.maku.mommons.Mommons.GSON;

@Service(listener = true)
public class PlayerDataRepository {

    public static final Map<String, CloudPlayer> PLAYERS = Maps.newConcurrentMap();

    @Load
    private DirectCloud directCloud;

    @BukkitEvent(PlayerJoinEvent.class)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PLAYERS.put(player.getName(), downloadCloudPlayer(player.getName()));
    }

    @BukkitEvent(PlayerQuitEvent.class)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PLAYERS.remove(player.getName());
    }

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
}
