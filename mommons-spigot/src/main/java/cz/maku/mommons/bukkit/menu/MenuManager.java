package cz.maku.mommons.bukkit.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager implements Listener {

    @Getter
    private static final Map<UUID, Menu> menus = new HashMap<>();

    public static void put(Player player, Menu menu) {
        menus.put(player.getUniqueId(), menu);
    }

    public static void open(Player player) {
        menus.get(player.getUniqueId()).open(player);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        UUID uuid = e.getWhoClicked().getUniqueId();

        Menu m = menus.get(uuid);
        if (m != null) {
            m.onClick(e);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        Menu m = menus.get(uuid);
        if (m != null) m.onOpenInv(e);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        Menu m = menus.get(uuid);
        if (m != null) {
            m.onCloseInv(e);
        }
    }
}
