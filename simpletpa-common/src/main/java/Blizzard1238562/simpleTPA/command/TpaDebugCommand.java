package Blizzard1238562.simpleTPA.command;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.listener.TpaDebugListener;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.util.MessageFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class TpaDebugCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final TpaDebugListener debugListener;
    private final Messenger messenger;

    public TpaDebugCommand(ConfigManager configManager, TpaDebugListener debugListener, Messenger messenger) {
        this.configManager = configManager;
        this.debugListener = debugListener;
        this.messenger = messenger;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tpa.debug")) {
            messenger.send(sender, MessageFormatter.parse(configManager.getMessage("no_permission")));
            return configManager.isUsageHintSuppressed();
        }

        if (debugListener.toggle()) {
            messenger.send(sender, MessageFormatter.parse(configManager.getMessage("tpa_debug_enabled")));
        } else {
            messenger.send(sender, MessageFormatter.parse(configManager.getMessage("tpa_debug_disabled")));
        }
        return true;
    }
}