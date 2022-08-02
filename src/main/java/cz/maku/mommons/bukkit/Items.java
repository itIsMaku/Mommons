package cz.maku.mommons.bukkit;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cz.maku.mommons.utils.Texts;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class Items {

    public static ItemStack create(@NotNull Material material, int amount, int data, String name, List<String> lore, boolean glow) {
        ItemStack stack = new ItemStack(material, amount, (short) data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack createBook(@NotNull String title, @NotNull String author, @NotNull List<String> pages) {
        ItemStack stack = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta meta = (BookMeta) stack.getItemMeta();
        meta.setGeneration(BookMeta.Generation.ORIGINAL);
        meta.setTitle(title);
        meta.setAuthor(author);
        meta.setPages(pages);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack createBook(@NotNull String title, @NotNull String author, @NotNull String... pages) {
        return createBook(title, author, Arrays.asList(pages));
    }

    public static ItemStack createHead(@NotNull String value) {
        if (!value.startsWith("http")) return getSkullFromWeb("https://textures.minecraft.net/texture/" + value);
        return getSkullFromWeb(value);
    }

    public static ItemStack createHead(@NotNull OfflinePlayer player) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setDisplayName(player.getName());
        meta.setOwner(player.getName());
        stack.setItemMeta(meta);
        return stack;
    }

    @SneakyThrows
    private static ItemStack getSkullFromWeb(@NotNull String url) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url: \"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        assert meta != null;
        profileField = meta.getClass().getDeclaredField("profile");
        profileField.setAccessible(true);
        profileField.set(meta, profile);
        stack.setItemMeta(meta);
        return stack;
    }

    public static boolean loreContains(@NotNull ItemMeta meta, CharSequence s) {
        if (meta.getLore() == null) return false;
        return Texts.contains(meta.getLore(), s);
    }

    public static int getNumberFromDisplayName(ItemStack item) {
        if (item.getItemMeta() == null) return 0;
        return Integer.parseInt(Texts.removeBukkitColors(item.getItemMeta().getDisplayName()).replaceAll("[^0-9?!\\.:]", ""));
    }
}
