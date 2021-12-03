package cz.maku.mommons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@Getter
public class Response {

    @Nullable
    private final Code code;
    @Nullable
    private final String content;

    public static boolean isValid(Response response) {
        return response != null && response.code == Code.SUCCESS;
    }

    public enum Code {
        SUCCESS,
        ERROR;
    }

}
