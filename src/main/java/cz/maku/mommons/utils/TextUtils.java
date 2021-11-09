package cz.maku.mommons.utils;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public final class TextUtils {

    public static final char ARROW = '➤';

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String formatClick(ChatColor color, String content) {
        return color + "[ " + ARROW + " " + content + " ]";
    }

    public static List<String> colorize(String... texts) {
        List<String> temp = Lists.newArrayList();
        for (String text : texts) {
            temp.add(colorize(text));
        }
        return temp;
    }

    public static List<String> textBlock(int averageLineLength, String text) {
        List<String> lines = Lists.newArrayList();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        boolean space = false;
        for (String word : words) {
            if (currentLine.length() >= averageLineLength) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
                space = false;
            }
            if (space) {
                currentLine.append(" ").append(word);
            } else {
                currentLine.append(word);
                space = true;
            }
        }
        if (currentLine.length() > 0) lines.add(currentLine.toString());
        AtomicReference<String> ccode = new AtomicReference<>(null);
        return lines.stream()
                .map(line -> {
                    if (ccode.get() != null) {
                        line = ccode + line;
                    }
                    if (!line.contains("§")) return line;
                    String[] lineS = line.split("§");
                    String appenderPart;
                    if ((appenderPart = lineS[lineS.length - 1]).length() > 0) {
                        ccode.set("§" + appenderPart.charAt(0));
                    }
                    return line;
                })
                .collect(Collectors.toList());
    }

}
