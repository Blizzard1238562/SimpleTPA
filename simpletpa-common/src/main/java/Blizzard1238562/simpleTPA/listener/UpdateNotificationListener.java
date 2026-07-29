package Blizzard1238562.simpleTPA.listener;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.update.ModrinthUpdateChecker;
import Blizzard1238562.simpleTPA.util.MessageFormatter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class UpdateNotificationListener implements Listener {

    private final ConfigManager configManager;
    private final ModrinthUpdateChecker updateChecker;
    private final Messenger messenger;

    public UpdateNotificationListener(ConfigManager configManager, ModrinthUpdateChecker updateChecker, Messenger messenger) {
        this.configManager = configManager;
        this.updateChecker = updateChecker;
        this.messenger = messenger;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!updateChecker.isUpdateAvailable()) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.isOp()) {
            return;
        }

        String message = configManager.getMessage("update_available_player")
                .replace("%version%", updateChecker.getLatestVersion())
                .replace("%url%", updateChecker.getLatestVersionUrl());
        messenger.send(player, MessageFormatter.parse(message));
    }
}
