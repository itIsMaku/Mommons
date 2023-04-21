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

    public static boolean isException(Response response) {
        return response instanceof ExceptionResponse;
    }

    @Nullable
    public static ExceptionResponse getExceptionResponse(Response response) {
        if (isException(response)) {
            return (ExceptionResponse) response;
        }
        return null;
    }

    public static Response from(Runnable runnable) {
        try {
            runnable.run();
            return new Response(Code.SUCCESS, "Success");
        } catch (Exception exception) {
            return new ExceptionResponse(Code.ERROR, "There was an exception", exception);
        }
    }

    public enum Code {
        SUCCESS,
        ERROR;
    }

}
