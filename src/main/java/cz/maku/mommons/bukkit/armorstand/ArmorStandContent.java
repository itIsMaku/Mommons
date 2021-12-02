package cz.maku.mommons.bukkit.armorstand;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public class ArmorStandContent {

    private final ItemStack head;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final ItemStack leftHand;
    private final ItemStack rightHand;

}
