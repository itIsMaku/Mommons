package cz.maku.mommons;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class ExceptionResponse extends Response {

    @NotNull
    private final Exception exception;

    public ExceptionResponse(@Nullable Code code, @Nullable String content, @NotNull Exception exception) {
        super(code, content);
        this.exception = exception;
    }
}
