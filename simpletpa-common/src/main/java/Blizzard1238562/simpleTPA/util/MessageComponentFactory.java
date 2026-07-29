package Blizzard1238562.simpleTPA.util;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public final class MessageComponentFactory {

    private final ConfigManager configManager;

    public MessageComponentFactory(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public Component createClickableButton(String type) {
        String text = configManager.getConfigValue("clickable_messages." + type + "_text", "[Button]");
        String hover = configManager.getConfigValue("clickable_messages." + type + "_hover", "Click to run this action.");
        String command = configManager.getConfigValue("clickable_messages." + type + "_command", "/help");
        NamedTextColor color = resolveColor(configManager.getConfigValue("clickable_messages." + type + "_color", "WHITE"));

        return MessageFormatter.parse(text)
                .colorIfAbsent(color)
                .clickEvent(ClickEvent.runCommand(command))
                .hoverEvent(HoverEvent.showText(MessageFormatter.parse(hover)));
    }

    private NamedTextColor resolveColor(String colorName) {
        NamedTextColor color = NamedTextColor.NAMES.value(colorName.toLowerCase(Locale.ROOT));
        return color != null ? color : NamedTextColor.WHITE;
    }
}
