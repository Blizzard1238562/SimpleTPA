package Blizzard1238562.simpleTPA.command;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.listener.LastLocationStore;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.platform.TeleportService;
import Blizzard1238562.simpleTPA.util.MessageFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpoCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final Messenger messenger;
    private final TeleportService teleportService;
    private final LastLocationStore lastLocationStore;

    public TpoCommand(ConfigManager configManager, Messenger messenger, TeleportService teleportService,
                       LastLocationStore lastLocationStore) {
        this.configManager = configManager;
        this.messenger = messenger;
        this.teleportService = teleportService;
        this.lastLocationStore = lastLocationStore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            messenger.send(sender, MessageFormatter.parse(configManager.getMessage("player_only_command")));
            return configManager.isUsageHintSuppressed();
        }

        if (!configManager.isTpoEnabled()) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpo_disabled")));
            return configManager.isUsageHintSuppressed();
        }

        if (!player.hasPermission("tpa.command.tpo")) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("no_permission")));
            return true;
        }

        if (args.length != 1) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("wrong_usage").replace("%command%", "tpo")));
            return configManager.isUsageHintSuppressed();
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target.isOnline()) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpo_player_online").replace("%player%", args[0])));
            return configManager.isUsageHintSuppressed();
        }

        Location lastLocation = lastLocationStore.getLastLocation(target.getUniqueId());
        if (lastLocation == null) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpo_no_data").replace("%player%", args[0])));
            return configManager.isUsageHintSuppressed();
        }

        teleportService.teleportToLocation(player, lastLocation);
        messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpo_success").replace("%player%", args[0])));
        return true;
    }
}
