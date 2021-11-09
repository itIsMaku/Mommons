package cz.maku.mommons.bukkit.menu;

import com.google.common.collect.Lists;
import cz.maku.mommons.bukkit.menu.exception.MenuContainerSizeException;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MenuContainer {

    private final Map<MenuElement, Integer> elements;
    private final List<MenuContainer> containers;
    private final int[] slots;

    public MenuContainer(int[] slots, List<MenuContainer> containers) {
        this.containers = containers;
        this.elements = new HashMap<>();
        this.slots = slots;
    }

    public MenuContainer(int[] slots) {
        this(slots, Lists.newArrayList());
    }

    public void addContainer(MenuContainer menuContainer) {
        containers.add(menuContainer);
    }

    public void addElement(MenuElement menuElement) {
        int max = Collections.max(elements.values());
        int slot = max + 1;
        setElement(slot, menuElement);
    }

    @SneakyThrows(MenuContainerSizeException.class)
    public void setElement(int slot, MenuElement menuElement) {
        if (!ArrayUtils.contains(slots, slot)) {
            throw new MenuContainerSizeException("Element out of container.");
        }
        elements.put(menuElement, slot);
    }

    public void clearContainer(Inventory inventory) {
        for (int slot : slots) {
            inventory.clear(slot);
        }
        elements.clear();
    }

}
