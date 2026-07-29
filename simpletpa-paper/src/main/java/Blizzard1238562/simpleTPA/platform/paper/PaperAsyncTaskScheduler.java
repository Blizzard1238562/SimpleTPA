package Blizzard1238562.simpleTPA.platform.paper;

import Blizzard1238562.simpleTPA.platform.AsyncTaskScheduler;
import Blizzard1238562.simpleTPA.platform.CancellableTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperAsyncTaskScheduler implements AsyncTaskScheduler {

    private final JavaPlugin plugin;

    public PaperAsyncTaskScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CancellableTask scheduleRepeatingAsync(Runnable task, long periodSeconds) {
        ScheduledTask scheduledTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, t -> task.run(), 0L, periodSeconds, TimeUnit.SECONDS);
        return scheduledTask::cancel;
    }

    @Override
    public void runOnMainContext(Runnable task) {
        Bukkit.getGlobalRegionScheduler().execute(plugin, task);
    }
}
