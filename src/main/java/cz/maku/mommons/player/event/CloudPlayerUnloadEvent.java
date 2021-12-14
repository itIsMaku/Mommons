package cz.maku.mommons.player.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class CloudPlayerUnloadEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public CloudPlayerUnloadEvent(@NotNull Player player) {
        super(player);
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
