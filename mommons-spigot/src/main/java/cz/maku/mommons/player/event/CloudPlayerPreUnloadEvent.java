package cz.maku.mommons.player.event;

import cz.maku.mommons.player.CloudPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class CloudPlayerPreUnloadEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final CloudPlayer cloudPlayer;

    public CloudPlayerPreUnloadEvent(@NotNull Player player, CloudPlayer cloudPlayer) {
        super(player);
        this.cloudPlayer = cloudPlayer;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}
