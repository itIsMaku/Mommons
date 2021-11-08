package cz.maku.mommons.worker.type;

import lombok.Data;

import java.util.function.Supplier;

@Data
public class ConditionResult {

    private final Supplier<Boolean> supplier;

    public static ConditionResult of(Supplier<Boolean> supplier) {
        return new ConditionResult(supplier);
    }

}
