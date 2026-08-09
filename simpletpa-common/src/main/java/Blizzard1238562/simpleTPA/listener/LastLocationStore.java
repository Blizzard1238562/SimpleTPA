package Blizzard1238562.simpleTPA.listener;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LastLocationStore implements Listener {

    private final Object lock = new Object();
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final File dataFile;
    private FileConfiguration data;

    public LastLocationStore(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.dataFile = new File(plugin.getDataFolder(), "playerlocations.yml");
        load();
    }

    private void load() {
        synchronized (lock) {
            if (!dataFile.exists()) {
                data = new YamlConfiguration();
                return;
            }
            data = YamlConfiguration.loadConfiguration(dataFile);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!configManager.isTpoEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        Location location = player.getLocation();

        synchronized (lock) {
            data.set(playerId.toString(), location);
            save();
        }
    }

    private void save() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save player location data: " + e.getMessage());
        }
    }

    public Location getLastLocation(UUID playerId) {
        synchronized (lock) {
            return data.getLocation(playerId.toString());
        }
    }
}
