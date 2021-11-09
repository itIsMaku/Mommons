package cz.maku.mommons.bukkit.menu;

import com.google.common.collect.Lists;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

@Data
public class Item {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public Item(Material material, int amount, int data) {
        this(new ItemStack(material, amount, (short) data));
    }

    public Item(Material material, int amount) {
        this(material, amount, 0);
    }

    public Item(Material material) {
        this(material, 1);
    }

    public Item(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public Item setName(String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    public Item setLore(String... lines) {
        return setLore(Arrays.asList(lines));
    }

    public Item setLore(List<String> lines) {
        List<String> lore = Lists.newArrayList();
        lore.addAll(lines);
        itemMeta.setLore(lore);
        return this;
    }

    public Item addLore(String line) {
        List<String> lore = itemMeta.getLore();
        lore.add(line);
        itemMeta.setLore(lore);
        return this;
    }

    public Item addEnchant(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public Item addFlags(ItemFlag... itemFlags) {
        itemMeta.addItemFlags(itemFlags);
        return this;
    }

    public Item glow(boolean state) {
        if (state) {
            addEnchant(Enchantment.DAMAGE_ALL, 1);
            return addFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemMeta.removeEnchant(Enchantment.DAMAGE_ALL);
        itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public Item build() {
        itemStack.setItemMeta(itemMeta);
        return this;
    }
}
