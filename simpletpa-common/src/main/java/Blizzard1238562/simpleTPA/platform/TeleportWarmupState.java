package Blizzard1238562.simpleTPA.platform;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.util.MessageFormatter;
import Blizzard1238562.simpleTPA.util.PlayerDisplayFormatter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class TeleportWarmupState {

    public enum TickResult {
        CONTINUE,
        CANCELLED,
        COMPLETE
    }

    private final Player requester;
    private final Player target;
    private final ConfigManager configManager;
    private final Messenger messenger;
    private final SoundPlayer soundPlayer;
    private final PlayerDisplayFormatter playerDisplayFormatter;
    private Location startLocation;
    private int remainingTicks;

    public TeleportWarmupState(Player requester, Player target, ConfigManager configManager, Messenger messenger,
                               SoundPlayer soundPlayer, PlayerDisplayFormatter playerDisplayFormatter) {
        this.requester = requester;
        this.target = target;
        this.configManager = configManager;
        this.messenger = messenger;
        this.soundPlayer = soundPlayer;
        this.playerDisplayFormatter = playerDisplayFormatter;
        this.remainingTicks = configManager.getTeleportWarmupSeconds() * 20;
    }

    public Player getRequester() {
        return requester;
    }

    public Player getTarget() {
        return target;
    }

    public TickResult tick() {
        if (!target.isOnline()) {
            notifyCancelled(target.getName());
            return TickResult.CANCELLED;
        }

        if (startLocation == null) {
            startLocation = requester.getLocation().clone();
        }

        if (hasMoved()) {
            notifyCancelled(playerDisplayFormatter.format(target));
            return TickResult.CANCELLED;
        }

        remainingTicks -= 1;
        if (remainingTicks <= 0) {
            return TickResult.COMPLETE;
        }
        return TickResult.CONTINUE;
    }

    public void notifyComplete() {
        messenger.send(requester, MessageFormatter.parse(configManager.getMessage("tpa_accept_teleport").replace("%player%", playerDisplayFormatter.format(target))));
        soundPlayer.play(requester, "tpa_accept");
    }

    public void notifyCancelledTargetGone() {
        notifyCancelled(target.getName());
    }

    private void notifyCancelled(String targetDisplayName) {
        messenger.send(requester, MessageFormatter.parse(configManager.getMessage("tpa_teleport_warmup_cancelled").replace("%player%", targetDisplayName)));
        soundPlayer.play(requester, "tpa_teleport_cancelled");
    }

    private boolean hasMoved() {
        Location current = requester.getLocation();
        return current.getBlockX() != startLocation.getBlockX()
                || current.getBlockY() != startLocation.getBlockY()
                || current.getBlockZ() != startLocation.getBlockZ()
                || !current.getWorld().equals(startLocation.getWorld());
    }
}
