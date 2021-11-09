package cz.maku.mommons.bukkit.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

@AllArgsConstructor
@Getter
public class MenuElement {

    private final Item item;
    private final BiConsumer<Player, InventoryClickEvent> action;
}