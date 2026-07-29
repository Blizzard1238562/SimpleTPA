package Blizzard1238562.simpleTPA.task;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.manager.TpaRequestManager;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.platform.SoundPlayer;
import Blizzard1238562.simpleTPA.util.MessageFormatter;
import Blizzard1238562.simpleTPA.util.PlayerDisplayFormatter;
import java.util.UUID;
import org.bukkit.entity.Player;

public final class TpaRequestExpirationTask implements Runnable {

    private final Player requester;
    private final Player target;
    private final TpaRequestManager requestManager;
    private final ConfigManager configManager;
    private final SoundPlayer soundPlayer;
    private final PlayerDisplayFormatter playerDisplayFormatter;
    private final Messenger messenger;

    public TpaRequestExpirationTask(Player requester, Player target, TpaRequestManager requestManager,
                                     ConfigManager configManager, SoundPlayer soundPlayer,
                                     PlayerDisplayFormatter playerDisplayFormatter, Messenger messenger) {
        this.requester = requester;
        this.target = target;
        this.requestManager = requestManager;
        this.configManager = configManager;
        this.soundPlayer = soundPlayer;
        this.playerDisplayFormatter = playerDisplayFormatter;
        this.messenger = messenger;
    }

    @Override
    public void run() {
        UUID senderId = requester.getUniqueId();
        UUID targetId = target.getUniqueId();
        if (!requestManager.isRequestStillPending(senderId, targetId)) {
            return;
        }

        requestManager.removeRequest(senderId, targetId);

        if (requester.isOnline()) {
            messenger.send(requester, MessageFormatter.parse(configManager.getMessage("tpa_request_expired_sender").replace("%target%", playerDisplayFormatter.format(target))));
            soundPlayer.play(requester, "tpa_expired");
        }

        if (target.isOnline()) {
            messenger.send(target, MessageFormatter.parse(configManager.getMessage("tpa_request_expired_receiver").replace("%player%", playerDisplayFormatter.format(requester))));
            soundPlayer.play(target, "tpa_expired");
        }
    }
}
