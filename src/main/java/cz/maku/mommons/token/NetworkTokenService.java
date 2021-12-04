package cz.maku.mommons.token;

import com.google.common.collect.Maps;
import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Response;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Service;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static cz.maku.mommons.Mommons.GSON;

@Service(sql = true, scheduled = true)
public class NetworkTokenService {

    private final Map<String, Consumer<NetworkTokenResponse>> actions = Maps.newConcurrentMap();

    public void addAction(String id, Consumer<NetworkTokenResponse> response) {
        actions.put(id, response);
    }

    public Response sendToken(String targetServer, Token token, int expire, ChronoUnit unit) {
        try {
            MySQL.getApi().query(
                    "mommons_networktoken",
                    "INSERT INTO {table} (target_server, token, token_data, action_id, expire, unit) VALUES (?, ?, ?, ?, ?, ?);",
                    targetServer, token.getToken(), GSON.toJson(token.getData()), token.getActionId(), expire, unit.name()
            );
            return new Response(Response.Code.SUCCESS, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ExceptionResponse(Response.Code.ERROR, "Exception while query.", e);
        }
    }

}