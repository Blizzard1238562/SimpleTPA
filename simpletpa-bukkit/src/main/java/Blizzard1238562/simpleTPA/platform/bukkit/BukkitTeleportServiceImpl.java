package Blizzard1238562.simpleTPA.platform.bukkit;

import Blizzard1238562.simpleTPA.platform.TeleportService;
import Blizzard1238562.simpleTPA.platform.TeleportWarmupState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class BukkitTeleportServiceImpl implements TeleportService {

    private final JavaPlugin plugin;

    public BukkitTeleportServiceImpl(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void teleport(Player toMove, Player destination) {
        toMove.teleport(destination);
    }

    @Override
    public void startWarmup(TeleportWarmupState state) {
        new BukkitRunnable() {
            @Override
            public void run() {
                TeleportWarmupState.TickResult result = state.tick();
                if (result == TeleportWarmupState.TickResult.CANCELLED) {
                    cancel();
                } else if (result == TeleportWarmupState.TickResult.COMPLETE) {
                    cancel();
                    teleport(state.getRequester(), state.getTarget());
                    state.notifyComplete();
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }
}
