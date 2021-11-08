package cz.maku.mommons.worker.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WorkerLoggerType {
    INFO(ConsoleColors.GREEN_BRIGHT + "INFO: ", ConsoleColors.GREEN_BRIGHT),
    WARNING(ConsoleColors.YELLOW + "WARNING: ", ConsoleColors.YELLOW),
    ERROR(ConsoleColors.RED_BRIGHT + "ERROR: ", ConsoleColors.RED_BRIGHT);

    private final String prefix;
    private final String color;
}
