package cz.maku.mommons.server;

import cz.maku.mommons.Response;
import cz.maku.mommons.player.CloudPlayer;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.BukkitCommand;
import cz.maku.mommons.worker.annotation.BukkitEvent;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Service;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("unused")
@Service(listener = true, commands = true)
public class ServerBukkitService {

    @Load
    private ServerDataService serverDataService;

    @BukkitEvent(AsyncPlayerPreLoginEvent.class)
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        if (!MySQL.getApi().isConnected()) {
            e.setKickMessage("§cChyba -> §7Na server se nelze připojit, nepodařilo se spojit s databází.");
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }
        if (serverDataService.getServer() == null) {
            e.setKickMessage("§cChyba -> §7Na server se nelze připojit, načítá se.");
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }

    @BukkitEvent(PlayerJoinEvent.class)
    public void onJoin(PlayerJoinEvent event) {
        serverDataService.getServer().setPlayers(Bukkit.getOnlinePlayers().size()).thenAccept(response -> {
            if (Response.isException(response) || !Response.isValid(response)) {
                event.getPlayer().kickPlayer("§cChyba -> §7Nepodařilo se aktualizovat počet hráčů na serveru.");
            }
        });
    }

    @BukkitEvent(PlayerQuitEvent.class)
    public void onQuit(PlayerQuitEvent event) {
        serverDataService.getServer().setPlayers(Bukkit.getOnlinePlayers().size() - 1).thenAccept(response -> {
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
