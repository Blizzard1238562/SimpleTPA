package Blizzard1238562.simpleTPA.command;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.manager.TpaRequestManager;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.platform.SoundPlayer;
import Blizzard1238562.simpleTPA.util.MessageFormatter;
import Blizzard1238562.simpleTPA.util.PlayerDisplayFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpaCancelCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final TpaRequestManager requestManager;
    private final SoundPlayer soundPlayer;
    private final PlayerDisplayFormatter playerDisplayFormatter;
    private final Messenger messenger;

    public TpaCancelCommand(ConfigManager configManager, TpaRequestManager requestManager, SoundPlayer soundPlayer,
                             PlayerDisplayFormatter playerDisplayFormatter, Messenger messenger) {
        this.configManager = configManager;
        this.requestManager = requestManager;
        this.soundPlayer = soundPlayer;
        this.playerDisplayFormatter = playerDisplayFormatter;
        this.messenger = messenger;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            messenger.send(sender, MessageFormatter.parse(configManager.getMessage("player_only_command")));
            return configManager.isUsageHintSuppressed();
        }

        if (!player.hasPermission("tpa.command.tpacancel")) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("no_permission")));
            return true;
        }

        Set<UUID> targetIds = requestManager.findTargetsForSender(player.getUniqueId());
        if (targetIds.isEmpty()) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_no_request")));
            return configManager.isUsageHintSuppressed();
        }

        UUID targetId;
        if (args.length >= 1) {
            Player namedTarget = Bukkit.getPlayer(args[0]);
            if (namedTarget == null || !targetIds.contains(namedTarget.getUniqueId())) {
                messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_no_request_to").replace("%player%", args[0])));
                return configManager.isUsageHintSuppressed();
            }
            targetId = namedTarget.getUniqueId();
        } else if (targetIds.size() == 1) {
            targetId = targetIds.iterator().next();
        } else {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_multiple_requests_outgoing").replace("%players%", formatTargetNames(targetIds))));
            return configManager.isUsageHintSuppressed();
        }

        requestManager.removeRequest(player.getUniqueId(), targetId);
        Player target = Bukkit.getPlayer(targetId);

        messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_cancel_success")));
        soundPlayer.play(player, "tpa_deny");

        if (target != null && target.isOnline()) {
            messenger.send(target, MessageFormatter.parse(configManager.getMessage("tpa_cancel_notify").replace("%player%", playerDisplayFormatter.format(player))));
        }
        return true;
    }

    private String formatTargetNames(Set<UUID> targetIds) {
        return targetIds.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .map(playerDisplayFormatter::format)
                .collect(Collectors.joining(", "));
    }
}
