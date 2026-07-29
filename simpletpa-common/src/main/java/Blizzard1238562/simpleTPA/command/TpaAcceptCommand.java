package Blizzard1238562.simpleTPA.command;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.manager.TpaRequestManager;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.platform.SoundPlayer;
import Blizzard1238562.simpleTPA.platform.TeleportService;
import Blizzard1238562.simpleTPA.platform.TeleportWarmupState;
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

public final class TpaAcceptCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final TpaRequestManager requestManager;
    private final SoundPlayer soundPlayer;
    private final PlayerDisplayFormatter playerDisplayFormatter;
    private final Messenger messenger;
    private final TeleportService teleportService;

    public TpaAcceptCommand(ConfigManager configManager, TpaRequestManager requestManager,
                             SoundPlayer soundPlayer, PlayerDisplayFormatter playerDisplayFormatter,
                             Messenger messenger, TeleportService teleportService) {
        this.configManager = configManager;
        this.requestManager = requestManager;
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

        if (!player.hasPermission("tpa.command.tpaccept")) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("no_permission")));
            return true;
        }

        Set<UUID> requesterIds = requestManager.findRequestersForTarget(player.getUniqueId());
        if (requesterIds.isEmpty()) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_no_request")));
            return configManager.isUsageHintSuppressed();
        }

        UUID requesterId;
        if (args.length >= 1) {
            Player namedRequester = Bukkit.getPlayer(args[0]);
            if (namedRequester == null || !requesterIds.contains(namedRequester.getUniqueId())) {
                messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_no_request_from").replace("%player%", args[0])));
                return configManager.isUsageHintSuppressed();
            }
            requesterId = namedRequester.getUniqueId();
        } else if (requesterIds.size() == 1) {
            requesterId = requesterIds.iterator().next();
        } else {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_multiple_requests").replace("%players%", formatRequesterNames(requesterIds))));
            return configManager.isUsageHintSuppressed();
        }

        Player requester = Bukkit.getPlayer(requesterId);
        requestManager.removeRequest(requesterId, player.getUniqueId());

        if (requester == null || !requester.isOnline()) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("player_not_online")));
            return configManager.isUsageHintSuppressed();
        }

        messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_accept_success").replace("%player%", playerDisplayFormatter.format(requester))));
        soundPlayer.play(player, "tpa_accept");

        if (configManager.isTeleportWarmupEnabled()) {
            int warmupSeconds = configManager.getTeleportWarmupSeconds();
            messenger.send(requester, MessageFormatter.parse(configManager.getMessage("tpa_teleport_warmup_started")
                    .replace("%player%", playerDisplayFormatter.format(player))
                    .replace("%seconds%", String.valueOf(warmupSeconds))));
            TeleportWarmupState warmupState = new TeleportWarmupState(requester, player, configManager, messenger, soundPlayer, playerDisplayFormatter);
            teleportService.startWarmup(warmupState);
        } else {
            teleportService.teleport(requester, player);
            messenger.send(requester, MessageFormatter.parse(configManager.getMessage("tpa_accept_teleport").replace("%player%", playerDisplayFormatter.format(player))));
            soundPlayer.play(requester, "tpa_accept");
        }

        return true;
    }

    private String formatRequesterNames(Set<UUID> requesterIds) {
        return requesterIds.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .map(playerDisplayFormatter::format)
                .collect(Collectors.joining(", "));
    }
}
