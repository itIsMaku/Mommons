package cz.maku.mommons.utils;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public final class Texts {

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

    public static List<String> createTextBlock(int averageLineLength, String text) {
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
        AtomicReference<String> code = new AtomicReference<>(null);
        return lines.stream()
                .map(line -> {
                    if (code.get() != null) {
                        line = code + line;
                    }
                    if (!line.contains("§")) return line;
                    String[] lineS = line.split("§");
                    String appenderPart;
                    if ((appenderPart = lineS[lineS.length - 1]).length() > 0) {
                        code.set("§" + appenderPart.charAt(0));
                    }
                    return line;
                })
                .collect(Collectors.toList());
    }

    public static boolean contains(Collection<String> collection, CharSequence s) {
        return collection.stream().filter(i -> i.contains(s)).findFirst().orElse(null) != null;
    }

    public static boolean isPositiveNumber(String s) {
        return s.matches("\\d+");
    }

    public static String getShortedClassName(Class<?> clazz) {
        return getShortedClassName(clazz.getName());
    }

    public static String getShortedClassName(String name) {
        String[] split = name.split("\\.");
        StringBuilder output = new StringBuilder();
        int i = 0;
        for (String s : split) {
            if ((i + 1) == split.length) {
                output.append(s);
                continue;
            }
            output.append(s.charAt(0)).append(".");
            i++;
        }
        return output.toString();
    }

    public static String removeBukkitColors(String string) {
        for (ChatColor value : ChatColor.values()) {
            string = string.replace("§" + value.getChar(), "");
        }
        return string;
    }

    public static String createBar(double required, double current, String completed, String need, int linesCount) {
        double progress_percentage = current / required;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < linesCount; i++) {
            if (i < linesCount * progress_percentage) {
                sb.append(completed);
            } else {
                sb.append(need);
            }
        }
        return sb.toString();
    }
}
