package Blizzard1238562.simpleTPA.command;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.platform.SoundPlayer;
import Blizzard1238562.simpleTPA.platform.TeleportService;
import Blizzard1238562.simpleTPA.util.MessageFormatter;
import Blizzard1238562.simpleTPA.util.PlayerDisplayFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpHereCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final SoundPlayer soundPlayer;
    private final PlayerDisplayFormatter playerDisplayFormatter;
    private final Messenger messenger;
    private final TeleportService teleportService;

    public TpHereCommand(ConfigManager configManager, SoundPlayer soundPlayer,
                          PlayerDisplayFormatter playerDisplayFormatter, Messenger messenger,
                          TeleportService teleportService) {
        this.configManager = configManager;
        this.soundPlayer = soundPlayer;
        this.playerDisplayFormatter = playerDisplayFormatter;
        this.messenger = messenger;
        this.teleportService = teleportService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            messenger.send(sender, MessageFormatter.parse(configManager.getMessage("player_only_command")));
            return configManager.isUsageHintSuppressed();
        }

        if (!player.hasPermission("tpa.command.tphere")) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("no_permission")));
            return true;
        }

        if (args.length != 1) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("wrong_usage").replace("%command%", "tphere")));
            return configManager.isUsageHintSuppressed();
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("player_not_online")));
            return configManager.isUsageHintSuppressed();
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_self_request")));
            return configManager.isUsageHintSuppressed();
        }

        teleportService.teleport(target, player);

        messenger.send(player, MessageFormatter.parse(configManager.getMessage("tphere_success").replace("%player%", playerDisplayFormatter.format(target))));
        messenger.send(target, MessageFormatter.parse(configManager.getMessage("tphere_moved_notify").replace("%player%", playerDisplayFormatter.format(player))));
        soundPlayer.play(player, "tpa_accept");
        soundPlayer.play(target, "tpa_accept");
        return true;
    }
}
