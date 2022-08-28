package cz.maku.mommons.bukkit;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class TextComponents {

    public static TextComponent create(@NotNull String text, @NotNull HoverEvent hoverEvent, @NotNull ClickEvent clickEvent) {
        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
        component.setHoverEvent(hoverEvent);
        component.setClickEvent(clickEvent);
        return component;
    }

    public static void send(@NotNull Player player, @NotNull String text, @NotNull HoverEvent hoverEvent, @NotNull ClickEvent clickEvent) {
        player.spigot().sendMessage(TextComponents.create(text, hoverEvent, clickEvent));
    }

}
