package Blizzard1238562.simpleTPA.platform;

import org.bukkit.entity.Player;

public interface TeleportService {

    void teleport(Player toMove, Player destination);

    void startWarmup(TeleportWarmupState state);
}
