package cz.maku.mommons.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class Menu {

    private final String title;
    private final int rows;
    private final Inventory inventory;
    private final List<MenuContainer> containers;
    private final Map<MenuElement, Integer> elements;

    public Menu(String title, int rows, List<MenuContainer> containers) {
        this.title = title;
        this.rows = rows;
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
        this.containers = containers;
        this.elements = new HashMap<>();
        for (MenuContainer container : containers) {
            for (Map.Entry<MenuElement, Integer> entry : container.getElements().entrySet()) {
                MenuElement element = entry.getKey();
                int slot = entry.getValue();
                inventory.setItem(slot, element.getItem().build().getItemStack());
            }
        }
    }

    public abstract void onOpen(InventoryOpenEvent e);

    public abstract void onClose(InventoryCloseEvent e);

    public void onOpenInv(InventoryOpenEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase(title)) return;
        onOpen(e);
    }

    public void onCloseInv(InventoryCloseEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase(title)) return;
        onClose(e);
    }

    public void addElement(MenuElement menuElement) {
        HashMap<Integer, ItemStack> slots = inventory.addItem(menuElement.getItem().getItemStack());
        for (Map.Entry<Integer, ItemStack> entry : slots.entrySet()) {
            elements.put(menuElement, entry.getKey());
        }
    }

    public void setElement(int slot, MenuElement menuElement) {
        inventory.setItem(slot, menuElement.getItem().getItemStack());
        elements.put(menuElement, slot);
    }

    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!e.getView().getTitle().equalsIgnoreCase(title)) return;
        if (e.getClickedInventory().equals(e.getView().getBottomInventory())) {
            e.setCancelled(true);
            return;
        }
        ItemStack is = e.getCurrentItem();
        if (is == null) return;
        for (MenuContainer container : containers) {
            for (MenuElement element : container.getElements().keySet()) {
                if (e.getSlot() != container.getElements().get(element)) continue;
                e.setCancelled(true);
                element.getAction().accept((Player) e.getView().getPlayer(), e);
            }
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}
