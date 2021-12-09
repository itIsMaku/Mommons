package cz.maku.mommons.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.temporal.ChronoUnit;
import java.util.Map;

@AllArgsConstructor
@Getter
public class NetworkTokenAction {

    private final String token;
    private final Map<String, String> data;
    private final String actionId;
    private final String targetServer;
    private final int expire;
    private final ChronoUnit unit;

}
