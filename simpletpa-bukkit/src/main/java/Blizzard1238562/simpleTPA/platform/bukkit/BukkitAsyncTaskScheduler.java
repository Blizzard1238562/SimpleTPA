package Blizzard1238562.simpleTPA.platform.bukkit;

import Blizzard1238562.simpleTPA.platform.AsyncTaskScheduler;
import Blizzard1238562.simpleTPA.platform.CancellableTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class BukkitAsyncTaskScheduler implements AsyncTaskScheduler {

    private final JavaPlugin plugin;

    public BukkitAsyncTaskScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CancellableTask scheduleRepeatingAsync(Runnable task, long periodSeconds) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, 0L, periodSeconds * 20L);
        return bukkitTask::cancel;
    }

    @Override
    public void runOnMainContext(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }
}
