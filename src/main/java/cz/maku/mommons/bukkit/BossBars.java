package cz.maku.mommons.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.plugin.java.JavaPlugin;

public final class BossBars {

    public static KeyedBossBar createBossBar(JavaPlugin javaPlugin, String namespace, String text, BarColor color, BarStyle barStyle, Sound sound, int minutes) {
        NamespacedKey namespacedKey = new NamespacedKey(javaPlugin, namespace);
        return Bukkit.createBossBar(namespacedKey, ChatColor.translateAlternateColorCodes('&', text), color, barStyle);
    }

}
