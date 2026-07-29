package Blizzard1238562.simpleTPA.platform.bukkit;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.platform.AbstractSoundPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;

public final class BukkitSoundPlayer extends AbstractSoundPlayer {

    private final BukkitAudiences audiences;

    public BukkitSoundPlayer(ConfigManager configManager, BukkitAudiences audiences) {
        super(configManager);
        this.audiences = audiences;
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

        audiences.player(player).playSound(Sound.sound(key, Sound.Source.PLAYER, 1.0F, 1.0F));
    }
}
