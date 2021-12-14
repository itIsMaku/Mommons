package cz.maku.mommons.token;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Response;
import cz.maku.mommons.loader.MommonsLoader;
import cz.maku.mommons.server.ServerDataService;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Async;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Repeat;
import cz.maku.mommons.worker.annotation.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static cz.maku.mommons.Mommons.GSON;

@Service(scheduled = true)
public class NetworkTokenService {

    private final Map<String, List<Consumer<NetworkTokenAction>>> actions = Maps.newConcurrentMap();

    @Load
    private ServerDataService serverDataService;

    public void addAction(String id, Consumer<NetworkTokenAction> actions) {
        List<Consumer<NetworkTokenAction>> tempActions = new ArrayList<>();
        tempActions.add(actions);
        if (this.actions.containsKey(id)) {
            tempActions.addAll(this.actions.get(id));
        }
        this.actions.put(id, tempActions);
    }

    public void addActions(String id, List<Consumer<NetworkTokenAction>> actions) {
        List<Consumer<NetworkTokenAction>> tempActions = new ArrayList<>(actions);
        if (this.actions.containsKey(id)) {
            tempActions.addAll(this.actions.get(id));
        }
        this.actions.put(id, tempActions);
    }

    public CompletableFuture<Response> sendTokens(String targetServer, List<Token> tokens, int expire, ChronoUnit unit) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                for (Token token : tokens) {
                    MySQL.getApi().query(
                            "mommons_networktokens",
                            "INSERT INTO {table} (target_server, token, token_data, action_id, expire, unit, executed, sent) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                            targetServer, token.getToken(), GSON.toJson(token.getData()), token.getActionId(), expire, unit.name(), 0, GSON.toJson(LocalDateTime.now())
                    );
                }
                return new Response(Response.Code.SUCCESS, null);
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionResponse(Response.Code.ERROR, "Exception while query.", e);
            }
        });
    }

    public CompletableFuture<Response> sendToken(String targetServer, Token token, int expire, ChronoUnit unit) {
        return sendTokens(targetServer, Collections.singletonList(token), expire, unit);
    }

    @Repeat(delay = 3 * 20, period = 30L)
    @Async
    public void download() {
        Logger logger = MommonsLoader.getPlugin().getLogger();
        MySQL.getApi().queryAsync("mommons_networktokens", "SELECT * FROM {table} WHERE target_server = ? AND executed = 0;", serverDataService.getServer().getId()).thenAccept(rows -> {
            if (rows.isEmpty()) return;
            for (SQLRow row : rows) {
                String target_server = row.getString("target_server");
                String token = row.getString("token");
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> token_data = GSON.fromJson(row.getString("token_data"), type);
                String action_id = row.getString("action_id");
                int expire = row.getInt("expire");
                ChronoUnit unit = ChronoUnit.valueOf(row.getString("unit").toUpperCase());
                LocalDateTime sent = GSON.fromJson(row.getString("sent"), LocalDateTime.class);
                if (unit.between(sent, LocalDateTime.now()) > expire) {
                    logger.warning("Token '" + token + "' expired during downloading.");
                    continue;
                }
                if (!actions.containsKey(action_id) || actions.get(action_id).isEmpty()) {
                    logger.severe("Action ID '" + action_id + "' does not have any registered action.");
                    continue;
                }
                Map<String, List<Consumer<NetworkTokenAction>>> actions = this.actions.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase(action_id)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                for (Map.Entry<String, List<Consumer<NetworkTokenAction>>> entry : actions.entrySet()) {
                    for (Consumer<NetworkTokenAction> consumer : entry.getValue()) {
                        consumer.accept(new NetworkTokenAction(token, token_data, action_id, target_server, expire, unit));
                    }
                }
            }
        });
    }

}