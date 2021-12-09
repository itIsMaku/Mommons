package cz.maku.mommons.storage.cloud;

import cz.maku.mommons.player.CloudPlayer;
import cz.maku.mommons.server.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DirectCloudStorage {
    SERVER(Server.class, "mommons_servers"),
    PLAYER(CloudPlayer.class, "mommons_players");

    private final Class<? extends CloudData> dataClass;
    private final String sqlTable;

}
