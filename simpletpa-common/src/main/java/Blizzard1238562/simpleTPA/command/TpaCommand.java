package Blizzard1238562.simpleTPA.command;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.event.TpaRequestSendEvent;
import Blizzard1238562.simpleTPA.manager.RequestType;
import Blizzard1238562.simpleTPA.manager.TpaRequestManager;
import Blizzard1238562.simpleTPA.platform.DelayedTaskScheduler;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.platform.SoundPlayer;
import Blizzard1238562.simpleTPA.task.TpaRequestExpirationTask;
import Blizzard1238562.simpleTPA.update.ModrinthUpdateChecker;
import Blizzard1238562.simpleTPA.util.MessageComponentFactory;
import Blizzard1238562.simpleTPA.util.MessageFormatter;
import Blizzard1238562.simpleTPA.util.PlayerDisplayFormatter;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TpaCommand implements CommandExecutor {

    private static final List<HelpEntry> HELP_ENTRIES = List.of(
            new HelpEntry("tpa", "tpa.command.tpa"),
            new HelpEntry("tpahere", "tpa.command.tpahere"),
            new HelpEntry("tpaccept", "tpa.command.tpaccept"),
            new HelpEntry("tpdeny", "tpa.command.tpdeny"),
            new HelpEntry("tpacancel", "tpa.command.tpacancel"),
            new HelpEntry("tpatoggle", "tpa.command.tpatoggle"),
            new HelpEntry("tphere", "tpa.command.tphere"),
            new HelpEntry("tpo", "tpa.command.tpo"),
            new HelpEntry("version", "tpa.command.version"),
            new HelpEntry("help", "tpa.command.help"),
            new HelpEntry("tpreload", "tpa.reload")
    );

    private final ConfigManager configManager;
    private final TpaRequestManager requestManager;
    private final SoundPlayer soundPlayer;
    private final MessageComponentFactory messageComponentFactory;
    private final ModrinthUpdateChecker updateChecker;
    private final PlayerDisplayFormatter playerDisplayFormatter;
    private final Messenger messenger;
    private final DelayedTaskScheduler delayedTaskScheduler;

    public TpaCommand(ConfigManager configManager, TpaRequestManager requestManager,
                       SoundPlayer soundPlayer, MessageComponentFactory messageComponentFactory,
                       ModrinthUpdateChecker updateChecker, PlayerDisplayFormatter playerDisplayFormatter,
                       Messenger messenger, DelayedTaskScheduler delayedTaskScheduler) {
        this.configManager = configManager;
        this.requestManager = requestManager;
        this.soundPlayer = soundPlayer;
        this.messageComponentFactory = messageComponentFactory;
        this.updateChecker = updateChecker;
        this.playerDisplayFormatter = playerDisplayFormatter;
        this.messenger = messenger;
        this.delayedTaskScheduler = delayedTaskScheduler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            if (!sender.hasPermission("tpa.command.version")) {
                messenger.send(sender, MessageFormatter.parse(configManager.getMessage("no_permission")));
                return true;
            }
            return handleVersion(sender);
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            if (!sender.hasPermission("tpa.command.help")) {
                messenger.send(sender, MessageFormatter.parse(configManager.getMessage("no_permission")));
                return true;
            }
            return handleHelp(sender);
        }

        if (!(sender instanceof Player player)) {
            messenger.send(sender, MessageFormatter.parse(configManager.getMessage("player_only_command")));
            return configManager.isUsageHintSuppressed();
        }

        if (!player.hasPermission("tpa.command.tpa")) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("no_permission")));
            return true;
        }

        return handleTpaRequest(player, args);
    }

    private boolean handleVersion(CommandSender sender) {
        String currentVersion = updateChecker.getCurrentVersion();
        String latestVersion = updateChecker.getLatestVersion();
        String resolvedLatest = latestVersion.isEmpty() ? currentVersion : latestVersion;

        String message = configManager.getMessage("version_info")
                .replace("%current%", currentVersion)
                .replace("%latest%", resolvedLatest)
                .replace("%url%", updateChecker.getLatestVersionUrl());
        messenger.send(sender, MessageFormatter.parse(message));
        return true;
    }

    private boolean handleTpaRequest(Player player, String[] args) {
        if (args.length != 1) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("wrong_usage").replace("%command%", "tpa")));
            return configManager.isUsageHintSuppressed();
        }

        UUID senderId = player.getUniqueId();
        int cooldownSeconds = configManager.getTpaCooldownSeconds();
        if (requestManager.isOnCooldown(senderId, cooldownSeconds)) {
            long remaining = requestManager.getRemainingCooldownSeconds(senderId, cooldownSeconds);
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_cooldown").replace("%seconds%", String.valueOf(remaining))));
            return configManager.isUsageHintSuppressed();
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("player_not_online")));
            return configManager.isUsageHintSuppressed();
        }

        if (target.getUniqueId().equals(senderId)) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_self_request")));
            return configManager.isUsageHintSuppressed();
        }

        if (requestManager.isOptedOut(target.getUniqueId())) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_target_not_accepting").replace("%target%", playerDisplayFormatter.format(target))));
            return configManager.isUsageHintSuppressed();
        }

        if (!configManager.isMultipleRequestsAllowed() && requestManager.hasPendingRequest(senderId)) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_request_exists")));
            return configManager.isUsageHintSuppressed();
        }

        if (requestManager.hasPendingRequestTo(senderId, target.getUniqueId())) {
            messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_request_exists_target")));
            return configManager.isUsageHintSuppressed();
        }

        // Event handling for TpaRequestSendEvent
        TpaRequestSendEvent event = new TpaRequestSendEvent(player, target, RequestType.TPA);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            if (event.getCancelReason() != null) {
                messenger.send(player, MessageFormatter.parse(event.getCancelReason()));
            }
            return configManager.isUsageHintSuppressed();
        }

        requestManager.createRequest(senderId, target.getUniqueId(), RequestType.TPA);
        requestManager.recordCooldown(senderId);

        messenger.send(player, MessageFormatter.parse(configManager.getMessage("tpa_request_sent").replace("%target%", playerDisplayFormatter.format(target))));
        sendRequestNotification(player, target);

        soundPlayer.play(player, "tpa_request_sent");
        soundPlayer.play(target, "tpa_request_received");

        TpaRequestExpirationTask expirationTask = new TpaRequestExpirationTask(player, target, requestManager, configManager, soundPlayer, playerDisplayFormatter, messenger, RequestType.TPA);
        long timeoutTicks = (long) configManager.getTpaRequestTimeoutSeconds() * 20L;
        delayedTaskScheduler.runDelayed(expirationTask, timeoutTicks);

        return true;
    }

    private boolean handleHelp(CommandSender sender) {
        messenger.send(sender, MessageFormatter.parse(configManager.getConfigValue("help_messages.header", "--- SimpleTPA Commands ---")));
        for (HelpEntry entry : HELP_ENTRIES) {
            if (entry.messageKey().equals("tpahere") && !configManager.isTpaHereEnabled()) {
                continue;
            }
            if (entry.messageKey().equals("tpo") && !configManager.isTpoEnabled()) {
                continue;
            }
            if (sender.hasPermission(entry.permission())) {
                messenger.send(sender, MessageFormatter.parse(configManager.getConfigValue("help_messages." + entry.messageKey(), entry.messageKey())));
            }
        }
        return true;
    }

    private void sendRequestNotification(Player player, Player target) {
        Component acceptButton = messageComponentFactory.createClickableButton("accept");
        Component denyButton = messageComponentFactory.createClickableButton("deny");

        Component message = MessageFormatter.parse(configManager.getMessage("tpa_request_received").replace("%player%", playerDisplayFormatter.format(player)) + " ")
                .colorIfAbsent(NamedTextColor.YELLOW)
                .append(acceptButton)
                .append(Component.text(" "))
                .append(denyButton);

        messenger.send(target, message);
    }

    private record HelpEntry(String messageKey, String permission) {
    }
}
