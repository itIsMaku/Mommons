package cz.maku.mommons.bukkit;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class Inventories {

    public static int getCountOfEmptySlots(Inventory inventory) {
        return getEmptySlots(inventory).length;
    }

    public static Integer[] getEmptySlots(Inventory inventory) {
        List<Integer> slots = Lists.newArrayList();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                slots.add(i);
            }
        }
        return slots.toArray(new Integer[0]);
    }

}
