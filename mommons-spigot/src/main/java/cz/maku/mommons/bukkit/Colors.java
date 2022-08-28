package cz.maku.mommons.bukkit;

import com.google.common.collect.Lists;
import cz.maku.mommons.utils.Texts;
import org.bukkit.ChatColor;

import java.util.List;

public final class Colors {

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String formatClick(ChatColor color, String content) {
        return color + "[ " + Texts.ARROW + " " + content + " ]";
    }

    public static List<String> colorize(String... texts) {
        List<String> temp = Lists.newArrayList();
        for (String text : texts) {
            temp.add(colorize(text));
        }
        return temp;
    }

    public static String removeBukkitColors(String string) {
        for (ChatColor value : ChatColor.values()) {
            string = string.replace("§" + value.getChar(), "");
        }
        return string;
    }
}
