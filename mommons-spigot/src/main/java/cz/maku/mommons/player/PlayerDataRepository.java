package cz.maku.mommons.player;

import com.google.common.collect.Maps;
import cz.maku.mommons.Response;
import cz.maku.mommons.player.event.CloudPlayerLoadEvent;
import cz.maku.mommons.server.ServerDataService;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Initialize;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static cz.maku.mommons.Mommons.GSON;

@Service
public class PlayerDataRepository {

    public static final Map<String, CloudPlayer> PLAYERS = Maps.newConcurrentMap();

    @Load
    private ServerDataService serverDataService;

    @Initialize
    public void initialize() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (PLAYERS.containsKey(player.getName())) continue;
            initializePlayerAsync(player.getName());
        }
    }

    @Nullable
    public CloudPlayer downloadCloudPlayer(String nickname) {
        Player player = Bukkit.getPlayer(nickname);
        MySQL.getApi().existRowAsync("mommons_players", "id", nickname).thenAcceptAsync(exist -> {
            if (!exist) {
                Response response = Response.from(() -> MySQL.getApi().query("mommons_players", "INSERT INTO {table} (id, data) VALUES (?, ?)", nickname, GSON.toJson(Maps.newHashMap())));
                if (Response.isException(response) || !Response.isValid(response)) {
                    if (player != null) {
                        player.kickPlayer("§cChyba -> §7Nastala chyba pri odesilani pozadavku na databazi.");
                    }
                }
            }
        });
        return new CloudPlayer(Maps.newHashMap(), Maps.newHashMap(), nickname, player);
    }

    protected void initializePlayerAsync(String name) {
        CloudPlayer cloudPlayer = downloadCloudPlayer(name);
        PLAYERS.put(name, cloudPlayer);
        cloudPlayer.setValueAsync("connected-server", serverDataService.getServer().getId(), true);
    }

    protected void load(Player player) {
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
}
