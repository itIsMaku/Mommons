package cz.maku.mommons.token;

import com.google.common.reflect.TypeToken;
import cz.maku.mommons.Mommons;
import cz.maku.mommons.plugin.MommonsPlugin;
import cz.maku.mommons.server.Server;
import cz.maku.mommons.server.ServerDataService;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Async;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Repeat;
import cz.maku.mommons.worker.annotation.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service(scheduled = true)
public class NetworkTokenWorker {

    @Load
    private NetworkTokenService networkTokenService;
    @Load
    private ServerDataService serverDataService;

    @Repeat(delay = 3 * 20, period = 30L)
    @Async
    public void download() {
        Server server = serverDataService.getServer();
        if (server == null) return;
        Logger logger = MommonsPlugin.getPlugin().getLogger();
        MySQL.getApi().queryAsync("mommons_networktokens", "SELECT * FROM {table} WHERE target_server = ? AND executed = 0;", server.getId()).thenAccept(rows -> {
            if (rows.isEmpty()) return;
            for (SQLRow row : rows) {
                String targetServer = row.getString("target_server");
                String token = row.getString("token");
                Map<String, String> tokenData = row.getJsonObject("token_data", new TypeToken<Map<String, String>>() {
                }.getType());
                String actionId = row.getString("action_id");
                int expire = row.getInt("expire");
                ChronoUnit unit = ChronoUnit.valueOf(row.getString("unit").toUpperCase());
                LocalDateTime sent = row.getJsonObject("sent", LocalDateTime.class);
                if (unit.between(sent, LocalDateTime.now()) > expire) {
                    logger.warning("Token '" + token + "' expired during downloading.");
                    tokenData.put("error", "expired");
                    MySQL.getApi().queryAsync("mommons_networktokens", "UPDATE {table} SET executed = 1, token_data = ? WHERE target_server = ? AND token = ?;", Mommons.GSON.toJson(tokenData), targetServer, token);
                    continue;
                }
                if (!networkTokenService.getActions().containsKey(actionId) || networkTokenService.getActions().get(actionId).isEmpty()) {
                    logger.severe("Action ID '" + actionId + "' does not have any registered action.");
                    tokenData.put("error", "action_not_found");
                    MySQL.getApi().queryAsync("mommons_networktokens", "UPDATE {table} SET executed = 1, token_data = ? WHERE target_server = ? AND token = ?;", Mommons.GSON.toJson(tokenData), targetServer, token);
                    continue;
                }
                MySQL.getApi().queryAsync("mommons_networktokens", "UPDATE {table} SET executed = 1 WHERE target_server = ? AND token = ?;", targetServer, token);
                Map<String, List<Consumer<NetworkTokenAction>>> actions = networkTokenService.getActions()
                        .entrySet()
                        .stream()
                        .filter(e -> e.getKey().equalsIgnoreCase(actionId))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                for (Map.Entry<String, List<Consumer<NetworkTokenAction>>> entry : actions.entrySet()) {
                    for (Consumer<NetworkTokenAction> consumer : entry.getValue()) {
                        consumer.accept(new NetworkTokenAction(token, tokenData, actionId, targetServer, expire, unit));
                    }
                }
            }
        });
    }

}
