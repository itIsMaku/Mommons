package cz.maku.mommons.token;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Token {

    private final String token;
    private final String actionId;
    private final Map<String, String> data;

    public static Token of(String actionId, Map<String, String> data) {
        return new Token(RandomStringUtils.randomAlphanumeric(8), actionId, data);
    }

}
