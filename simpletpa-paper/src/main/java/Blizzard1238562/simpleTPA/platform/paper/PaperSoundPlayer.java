package Blizzard1238562.simpleTPA.platform.paper;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.platform.AbstractSoundPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;

public final class PaperSoundPlayer extends AbstractSoundPlayer {

    public PaperSoundPlayer(ConfigManager configManager) {
        super(configManager);
    }

    @Override
    public void play(Player player, String soundKey) {
        String rawName = getConfiguredSoundName(soundKey);
        if (rawName.isEmpty()) {
            return;
        }

        Key key = resolveKey(rawName);
        if (key == null) {
            configManager.getLogger().warning("Invalid sound: " + rawName);
            return;
        }

        player.playSound(Sound.sound(key, Sound.Source.PLAYER, 1.0F, 1.0F));
    }
}
