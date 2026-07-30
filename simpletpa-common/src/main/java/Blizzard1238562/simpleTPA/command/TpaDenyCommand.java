package Blizzard1238562.simpleTPA.command;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.manager.PendingRequest;
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

public final class TpaDenyCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final TpaRequestManager requestManager;
    private final SoundPlayer soundPlayer;
    private final PlayerDisplayFormatter playerDisplayFormatter;
    private final Messenger messenger;

    public TpaDenyCommand(ConfigManager configManager, TpaRequestManager requestManager, SoundPlayer soundPlayer,
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

        if (!player.hasPermission("tpa.command.tpdeny")) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("no_permission")));
            return true;
        }

        Set<PendingRequest> incomingRequests = requestManager.findRequestersForTarget(player.getUniqueId());
        if (incomingRequests.isEmpty()) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_no_request")));
            return configManager.isUsageHintSuppressed();
        }

        UUID requesterId;
        if (args.length >= 1) {
            Player namedRequester = Bukkit.getPlayer(args[0]);
            PendingRequest chosen = namedRequester == null ? null : findByOtherPlayer(incomingRequests, namedRequester.getUniqueId());
            if (chosen == null) {
                messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_no_request_from").replace("%player%", args[0])));
                return configManager.isUsageHintSuppressed();
            }
            requesterId = chosen.otherPlayerId();
        } else if (incomingRequests.size() == 1) {
            requesterId = incomingRequests.iterator().next().otherPlayerId();
        } else {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_multiple_requests").replace("%players%", formatNames(incomingRequests))));
            return configManager.isUsageHintSuppressed();
        }

        Player requester = Bukkit.getPlayer(requesterId);
        requestManager.removeRequest(requesterId, player.getUniqueId());

        if (requester != null && requester.isOnline()) {
            messenger.send(requester, MessageFormatter.parse(configManager.getMessage("tpa_deny_notify").replace("%player%", playerDisplayFormatter.format(player))));
            soundPlayer.play(requester, "tpa_deny");
        }

        String requesterName = requester != null ? playerDisplayFormatter.format(requester) : "unknown";
        messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_deny_success").replace("%player%", requesterName)));
        soundPlayer.play(player, "tpa_deny");
        return true;
    }

    private PendingRequest findByOtherPlayer(Set<PendingRequest> requests, UUID otherPlayerId) {
        for (PendingRequest request : requests) {
            if (request.otherPlayerId().equals(otherPlayerId)) {
                return request;
            }
        }
        return null;
    }

    private String formatNames(Set<PendingRequest> requests) {
        return requests.stream()
                .map(PendingRequest::otherPlayerId)
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .map(playerDisplayFormatter::format)
                .collect(Collectors.joining(", "));
    }
}
