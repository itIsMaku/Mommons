package cz.maku.mommons.token;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Mommons;
import cz.maku.mommons.Response;
import cz.maku.mommons.plugin.MommonsPlugin;
import cz.maku.mommons.server.Server;
import cz.maku.mommons.server.ServerDataService;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Async;
import cz.maku.mommons.worker.annotation.Load;
import cz.maku.mommons.worker.annotation.Repeat;
import cz.maku.mommons.worker.annotation.Service;
import lombok.AccessLevel;
import lombok.Getter;

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

@Service
public class NetworkTokenService {

    @Getter(AccessLevel.PROTECTED)
    private final Map<String, List<Consumer<NetworkTokenAction>>> actions = Maps.newConcurrentMap();

    public void addAction(String id, Consumer<NetworkTokenAction> action) {
        addActions(id, Collections.singletonList(action));
    }

    public void addActions(String id, List<Consumer<NetworkTokenAction>> actions) {
        List<Consumer<NetworkTokenAction>> tempActions = new ArrayList<>(actions);
        if (this.actions.containsKey(id)) {
            tempActions.addAll(this.actions.get(id));
        }
        this.actions.put(id, tempActions);
    }

    public CompletableFuture<Response> sendTokens(String targetServer, List<Token> tokens, int expire, ChronoUnit unit) {
        return CompletableFuture.supplyAsync(() -> sendTokensSync(targetServer, tokens, expire, unit), Mommons.ES);
    }

    public Response sendTokensSync(String targetServer, List<Token> tokens, int expire, ChronoUnit unit) {
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
    }

    public CompletableFuture<Response> sendToken(String targetServer, Token token, int expire, ChronoUnit unit) {
        return sendTokens(targetServer, Collections.singletonList(token), expire, unit);
    }

    public Response sendTokenSync(String targetServer, Token token, int expire, ChronoUnit unit) {
        return sendTokensSync(targetServer, Collections.singletonList(token), expire, unit);
    }

}