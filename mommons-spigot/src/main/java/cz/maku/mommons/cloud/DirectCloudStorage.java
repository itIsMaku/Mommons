package cz.maku.mommons.cloud;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

@AllArgsConstructor
@Getter
@Deprecated
@ApiStatus.ScheduledForRemoval
public enum DirectCloudStorage {
    SERVER(null, "mommons_servers"),
    PLAYER(null, "mommons_players");

    private final Class<? extends CloudData> dataClass;
    private final String sqlTable;

}