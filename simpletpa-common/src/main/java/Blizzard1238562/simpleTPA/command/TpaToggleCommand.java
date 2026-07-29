package Blizzard1238562.simpleTPA.command;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.manager.TpaRequestManager;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.platform.SoundPlayer;
import Blizzard1238562.simpleTPA.util.MessageFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpaToggleCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final TpaRequestManager requestManager;
    private final SoundPlayer soundPlayer;
    private final Messenger messenger;

    public TpaToggleCommand(ConfigManager configManager, TpaRequestManager requestManager, SoundPlayer soundPlayer,
                             Messenger messenger) {
        this.configManager = configManager;
        this.requestManager = requestManager;
        this.soundPlayer = soundPlayer;
        this.messenger = messenger;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            messenger.send(sender, MessageFormatter.parse(configManager.getMessage("player_only_command")));
            return configManager.isUsageHintSuppressed();
        }

        if (!player.hasPermission("tpa.command.tpatoggle")) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("no_permission")));
            return true;
        }

        boolean nowOptedOut = requestManager.toggleOptOut(player.getUniqueId());
        if (nowOptedOut) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_toggle_enabled")));
            soundPlayer.play(player, "tpa_toggle_enabled");
        } else {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_toggle_disabled")));
            soundPlayer.play(player, "tpa_toggle_disabled");
        }
        return true;
    }
}
