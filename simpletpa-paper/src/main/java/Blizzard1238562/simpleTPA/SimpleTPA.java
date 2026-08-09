package Blizzard1238562.simpleTPA;

import Blizzard1238562.simpleTPA.command.TpaAcceptCommand;
import Blizzard1238562.simpleTPA.command.TpaCancelCommand;
import Blizzard1238562.simpleTPA.command.TpaCommand;
import Blizzard1238562.simpleTPA.command.TpaDebugCommand;
import Blizzard1238562.simpleTPA.command.TpaDenyCommand;
import Blizzard1238562.simpleTPA.command.TpaHereCommand;
import Blizzard1238562.simpleTPA.command.TpaReloadCommand;
import Blizzard1238562.simpleTPA.command.TpaToggleCommand;
import Blizzard1238562.simpleTPA.command.TpHereCommand;
import Blizzard1238562.simpleTPA.command.TpoCommand;
import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.listener.LastLocationStore;
import Blizzard1238562.simpleTPA.listener.TpaDebugListener;
import Blizzard1238562.simpleTPA.listener.UpdateNotificationListener;
import Blizzard1238562.simpleTPA.manager.TpaRequestManager;
import Blizzard1238562.simpleTPA.platform.DelayedTaskScheduler;
import Blizzard1238562.simpleTPA.platform.Messenger;
import Blizzard1238562.simpleTPA.platform.SoundPlayer;
import Blizzard1238562.simpleTPA.platform.TeleportService;
import Blizzard1238562.simpleTPA.platform.paper.PaperAsyncTaskScheduler;
import Blizzard1238562.simpleTPA.platform.paper.PaperDelayedTaskScheduler;
import Blizzard1238562.simpleTPA.platform.paper.PaperMessenger;
import Blizzard1238562.simpleTPA.platform.paper.PaperSoundPlayer;
import Blizzard1238562.simpleTPA.platform.paper.PaperTeleportService;
import Blizzard1238562.simpleTPA.update.ModrinthUpdateChecker;
import Blizzard1238562.simpleTPA.util.MessageComponentFactory;
import Blizzard1238562.simpleTPA.util.PlayerDisplayFormatter;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleTPA extends JavaPlugin {

    private ConfigManager configManager;
    private ModrinthUpdateChecker updateChecker;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.load();

        TpaRequestManager requestManager = new TpaRequestManager();
        Messenger messenger = new PaperMessenger();
        SoundPlayer soundPlayer = new PaperSoundPlayer(configManager);
        MessageComponentFactory messageComponentFactory = new MessageComponentFactory(configManager);
        PlayerDisplayFormatter playerDisplayFormatter = new PlayerDisplayFormatter(configManager);
        DelayedTaskScheduler delayedTaskScheduler = new PaperDelayedTaskScheduler(this);
        TeleportService teleportService = new PaperTeleportService(this);
        TpaDebugListener debugListener = new TpaDebugListener(this);
        LastLocationStore lastLocationStore = new LastLocationStore(this, configManager);
        
        updateChecker = new ModrinthUpdateChecker(this, configManager, new PaperAsyncTaskScheduler(this));

        registerCommands(requestManager, soundPlayer, messageComponentFactory, playerDisplayFormatter, messenger,
                delayedTaskScheduler, teleportService, debugListener, lastLocationStore);
        getServer().getPluginManager().registerEvents(
                new UpdateNotificationListener(configManager, updateChecker, messenger), this);
        getServer().getPluginManager().registerEvents(debugListener, this);
        getServer().getPluginManager().registerEvents(lastLocationStore, this);

        updateChecker.start();
        getLogger().info("SimpleTPA Activated!");
    }

    @Override
    public void onDisable() {
        if (updateChecker != null) {
            updateChecker.stop();
        }
    }

    private void registerCommands(TpaRequestManager requestManager, SoundPlayer soundPlayer,
                                   MessageComponentFactory messageComponentFactory,
                                   PlayerDisplayFormatter playerDisplayFormatter, Messenger messenger,
                                   DelayedTaskScheduler delayedTaskScheduler, TeleportService teleportService,
                                   TpaDebugListener debugListener, LastLocationStore lastLocationStore) {
        getCommand("tpa").setExecutor(new TpaCommand(
                configManager, requestManager, soundPlayer, messageComponentFactory, updateChecker,
                playerDisplayFormatter, messenger, delayedTaskScheduler));
        getCommand("tpahere").setExecutor(new TpaHereCommand(
                configManager, requestManager, soundPlayer, messageComponentFactory,
                playerDisplayFormatter, messenger, delayedTaskScheduler));
        getCommand("tpaccept").setExecutor(new TpaAcceptCommand(configManager, requestManager, soundPlayer,
                playerDisplayFormatter, messenger, teleportService));
        getCommand("tpdeny").setExecutor(new TpaDenyCommand(configManager, requestManager, soundPlayer,
                playerDisplayFormatter, messenger));
        getCommand("tpacancel").setExecutor(new TpaCancelCommand(configManager, requestManager, soundPlayer,
                playerDisplayFormatter, messenger));
        getCommand("tpatoggle").setExecutor(new TpaToggleCommand(configManager, requestManager, soundPlayer, messenger));
        getCommand("tpreload").setExecutor(new TpaReloadCommand(configManager, updateChecker, messenger));
        getCommand("tpdebug").setExecutor(new TpaDebugCommand(configManager, debugListener, messenger));
        getCommand("tphere").setExecutor(new TpHereCommand(configManager, soundPlayer, playerDisplayFormatter, messenger, teleportService));
        getCommand("tpo").setExecutor(new TpoCommand(configManager, messenger, teleportService, lastLocationStore));
    }
}
