package cz.maku.mommons.utils;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public final class Texts {

    public static final char ARROW = '➤';
    public static final char ARROW_RIGHT = '➜';
    public static final char ARROW_LEFT = '⬅';
    public static final char ARROW_UP = '⬆';
    public static final char ARROW_DOWN = '⬇';
    public static final char CORRECT = '✓';
    public static final char INCORRECT = '✗';
    public static final char STAR = '★';

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
            if ((i + 2) == split.length) {
                output.append(s).append(".");
                i++;
                continue;
            }
            output.append(s.charAt(0)).append(".");
            i++;
        }
        return output.toString();
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

    public static String underscore(String s) {
        StringBuilder temp = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                temp.append("_");
            }
            temp.append(Character.toLowerCase(c));
        }
        return temp.toString();
    }
}
