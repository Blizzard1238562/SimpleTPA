package Blizzard1238562.simpleTPA.platform;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import java.util.Locale;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

public abstract class AbstractSoundPlayer implements SoundPlayer {

    protected final ConfigManager configManager;

    protected AbstractSoundPlayer(ConfigManager configManager) {
        this.configManager = configManager;
    }

    protected final String getConfiguredSoundName(String soundKey) {
        return configManager.getConfigValue("sounds." + soundKey, "");
    }

    protected final Key resolveKey(String rawName) {
        String normalized = rawName.toLowerCase(Locale.ROOT);
        if (normalized.startsWith("minecraft:")) {
            normalized = normalized.substring("minecraft:".length());
        }

        if (Registry.SOUNDS.get(NamespacedKey.minecraft(normalized)) != null) {
            return Key.key("minecraft", normalized);
        }

        String dotted = normalized.replace('_', '.');
        if (Registry.SOUNDS.get(NamespacedKey.minecraft(dotted)) != null) {
            return Key.key("minecraft", dotted);
        }

        return null;
    }
}
