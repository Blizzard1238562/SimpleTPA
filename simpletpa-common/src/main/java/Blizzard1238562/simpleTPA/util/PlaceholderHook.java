package Blizzard1238562.simpleTPA.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class PlaceholderHook {

    private PlaceholderHook() {
    }

    public static boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static String apply(Player player, String text) {
        if (!isAvailable()) {
            return text;
        }
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
