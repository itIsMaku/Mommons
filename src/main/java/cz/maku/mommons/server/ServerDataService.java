package cz.maku.mommons.server;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Response;
import cz.maku.mommons.storage.cloud.DirectCloud;
import cz.maku.mommons.storage.cloud.DirectCloudStorage;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.WorkerReceiver;
import cz.maku.mommons.worker.annotation.BukkitEvent;
import cz.maku.mommons.worker.annotation.Initialize;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Service;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Type;
import java.util.Map;

import static cz.maku.mommons.Mommons.GSON;

@Service(listener = true)
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
        String id = coreConfiguration.getString("server.id");
        Object object = directCloud.get(DirectCloudStorage.SERVER, "id", id, "data");
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        if (object == null) {
            object = GSON.toJson(Maps.newHashMap());
        }
        server = new Server(id, GSON.fromJson((String) object, type), Maps.newConcurrentMap());
        localServerInfo = new LocalServerInfo();
    }

    @BukkitEvent(AsyncPlayerPreLoginEvent.class)
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
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
}
