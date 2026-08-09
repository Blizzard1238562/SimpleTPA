package Blizzard1238562.simpleTPA.platform.paper;

import Blizzard1238562.simpleTPA.platform.TeleportService;
import Blizzard1238562.simpleTPA.platform.TeleportWarmupState;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperTeleportService implements TeleportService {

    private final JavaPlugin plugin;

    public PaperTeleportService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void teleport(Player toMove, Player destination) {
        toMove.teleportAsync(destination.getLocation());
    }

    @Override
    public void teleportToLocation(Player toMove, Location destination) {
        toMove.teleportAsync(destination);
    }

    @Override
    public void startWarmup(TeleportWarmupState state) {
        Player requester = state.getRequester();
        Player target = state.getTarget();

        requester.getScheduler().runAtFixedRate(plugin, scheduledTask -> tick(scheduledTask, state, target), null, 1L, 1L);
    }

    private void tick(ScheduledTask scheduledTask, TeleportWarmupState state, Player target) {
        TeleportWarmupState.TickResult result = state.tick();
        if (result == TeleportWarmupState.TickResult.CANCELLED) {
            scheduledTask.cancel();
        } else if (result == TeleportWarmupState.TickResult.COMPLETE) {
            scheduledTask.cancel();
            completeTeleport(state, target);
        }
    }

    private void completeTeleport(TeleportWarmupState state, Player target) {
        Player requester = state.getRequester();
        target.getScheduler().run(plugin, targetTask -> {
            teleport(requester, target);
            state.notifyComplete();
        }, state::notifyCancelledTargetGone);
    }
}
