package Blizzard1238562.simpleTPA.command;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.update.ModrinthUpdateChecker;
import Blizzard1238562.simpleTPA.util.MessageFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class TpaReloadCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final ModrinthUpdateChecker updateChecker;
    private final Messenger messenger;

    public TpaReloadCommand(ConfigManager configManager, ModrinthUpdateChecker updateChecker, Messenger messenger) {
        this.configManager = configManager;
        this.updateChecker = updateChecker;
        this.messenger = messenger;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tpa.reload")) {
            messenger.send(sender, MessageFormatter.parse(configManager.getMessage("no_permission")));
            return configManager.isUsageHintSuppressed();
        }

        configManager.reload();
        updateChecker.start();
        messenger.send(sender, MessageFormatter.parse(configManager.getMessage("config_reloaded")));
        return true;
    }
}
