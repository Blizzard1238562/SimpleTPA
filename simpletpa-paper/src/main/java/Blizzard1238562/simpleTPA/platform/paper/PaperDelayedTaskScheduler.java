package Blizzard1238562.simpleTPA.platform.paper;

import Blizzard1238562.simpleTPA.platform.DelayedTaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperDelayedTaskScheduler implements DelayedTaskScheduler {

    private final JavaPlugin plugin;

    public PaperDelayedTaskScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runDelayed(Runnable task, long delayTicks) {
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> task.run(), Math.max(1L, delayTicks));
    }
}
