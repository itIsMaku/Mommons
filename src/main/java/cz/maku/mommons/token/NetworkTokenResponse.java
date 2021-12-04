package cz.maku.mommons.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class NetworkTokenResponse {

    private final Map<String, String> data;

}
