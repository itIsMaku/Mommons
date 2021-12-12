package cz.maku.mommons.player.event;

import cz.maku.mommons.player.CloudPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class CloudPlayerLoadEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final CloudPlayer cloudPlayer;
    @Setter
    private boolean cancel;

    public CloudPlayerLoadEvent(@NotNull Player player, CloudPlayer cloudPlayer) {
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
