package Blizzard1238562.simpleTPA.platform.bukkit;

import Blizzard1238562.simpleTPA.platform.DelayedTaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitDelayedTaskScheduler implements DelayedTaskScheduler {

    private final JavaPlugin plugin;

    public BukkitDelayedTaskScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runDelayed(Runnable task, long delayTicks) {
        Bukkit.getScheduler().runTaskLater(plugin, task, Math.max(1L, delayTicks));
    }
}
