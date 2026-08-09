package Blizzard1238562.simpleTPA.platform;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface TeleportService {

    void teleport(Player toMove, Player destination);

    void teleportToLocation(Player toMove, Location destination);

    void startWarmup(TeleportWarmupState state);
}
