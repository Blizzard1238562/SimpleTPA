package Blizzard1238562.simpleTPA.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class MessageFormatter {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    private static final Map<Character, String> COLOR_TAGS = Map.ofEntries(
            Map.entry('0', "black"),
            Map.entry('1', "dark_blue"),
            Map.entry('2', "dark_green"),
            Map.entry('3', "dark_aqua"),
            Map.entry('4', "dark_red"),
            Map.entry('5', "dark_purple"),
            Map.entry('6', "gold"),
            Map.entry('7', "gray"),
            Map.entry('8', "dark_gray"),
            Map.entry('9', "blue"),
            Map.entry('a', "green"),
            Map.entry('b', "aqua"),
            Map.entry('c', "red"),
            Map.entry('d', "light_purple"),
            Map.entry('e', "yellow"),
            Map.entry('f', "white")
    );

    private static final Map<Character, String> DECORATION_TAGS = Map.of(
            'k', "obfuscated",
            'l', "bold",
            'm', "strikethrough",
            'n', "underlined",
            'o', "italic"
    );

    private MessageFormatter() {
    }

    public static Component parse(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        String converted = convertLegacyToMiniMessage(text);
        try {
            return MINI_MESSAGE.deserialize(converted);
        } catch (Exception e) {
            return LEGACY_SERIALIZER.deserialize(text);
        }
    }

    private static String convertLegacyToMiniMessage(String text) {
        if (text.indexOf('\u00A7') < 0) {
            return text;
        }

        StringBuilder result = new StringBuilder(text.length() + 16);
        Deque<String> openDecorations = new ArrayDeque<>();

        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
            if (c == '\u00A7' && i + 1 < text.length()) {
                char code = Character.toLowerCase(text.charAt(i + 1));
                String colorTag = COLOR_TAGS.get(code);
                if (colorTag != null) {
                    closeDecorations(result, openDecorations);
                    result.append('<').append(colorTag).append('>');
                    i += 2;
                    continue;
                }

                String decorationTag = DECORATION_TAGS.get(code);
                if (decorationTag != null) {
                    result.append('<').append(decorationTag).append('>');
                    openDecorations.push(decorationTag);
                    i += 2;
                    continue;
                }

                if (code == 'r') {
                    closeDecorations(result, openDecorations);
                    result.append("<reset>");
                    i += 2;
                    continue;
                }
            }

            result.append(c);
            i++;
        }

        return result.toString();
    }

    private static void closeDecorations(StringBuilder result, Deque<String> openDecorations) {
        while (!openDecorations.isEmpty()) {
            result.append("</").append(openDecorations.pop()).append('>');
        }
    }
}
