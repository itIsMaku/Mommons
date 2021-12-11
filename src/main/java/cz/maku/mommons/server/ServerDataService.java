package cz.maku.mommons.server;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.Response;
import cz.maku.mommons.loader.MommonsLoader;
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
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

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
        }, 20 * 1);
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
