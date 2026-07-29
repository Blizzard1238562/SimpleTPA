package Blizzard1238562.simpleTPA.util;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import org.bukkit.entity.Player;

public final class PlayerDisplayFormatter {

    private final ConfigManager configManager;

    public PlayerDisplayFormatter(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public String format(Player player) {
        if (player == null) {
            return "";
        }

        if (!configManager.isPlaceholderApiFormattingEnabled() || !PlaceholderHook.isAvailable()) {
            return player.getName();
        }

        String template = configManager.getPlayerDisplayFormat();
        if (template == null || template.isEmpty()) {
            return player.getName();
        }

        String withName = template.replace("%player%", player.getName());
        try {
            return PlaceholderHook.apply(player, withName);
        } catch (Exception e) {
            return player.getName();
        }
    }
}
