package cz.maku.mommons.cache;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ExpirableValue<T> {

    private final int expireAfter;
    private final ChronoUnit unit;
    @Nullable
    private T value;
    private LocalDateTime lastTime;

    public ExpirableValue(int expireAfter, ChronoUnit unit) {
        this.expireAfter = expireAfter;
        this.unit = unit;
        lastTime = LocalDateTime.now();
    }

    public void renew(T value) {
        this.value = value;
        lastTime = LocalDateTime.now();
    }

    public void clear() {
        value = null;
        lastTime = null;
    }

    public T getValue() {
        if (unit.between(lastTime, LocalDateTime.now()) >= expireAfter) {
            value = null;
            lastTime = null;
        }
        return value;
    }
}
