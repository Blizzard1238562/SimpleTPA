package Blizzard1238562.simpleTPA.update;

import Blizzard1238562.simpleTPA.config.ConfigManager;
import Blizzard1238562.simpleTPA.platform.AsyncTaskScheduler;
import Blizzard1238562.simpleTPA.platform.CancellableTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModrinthUpdateChecker {

    private static final long CHECK_INTERVAL_SECONDS = 43200L;

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final AsyncTaskScheduler asyncTaskScheduler;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private CancellableTask task;
    private volatile boolean updateAvailable = false;
    private volatile String latestVersion = "";
    private volatile String latestVersionUrl = "https://modrinth.com/plugin/simpletpaplugin";

    public ModrinthUpdateChecker(JavaPlugin plugin, ConfigManager configManager, AsyncTaskScheduler asyncTaskScheduler) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.asyncTaskScheduler = asyncTaskScheduler;
    }

    public void start() {
        stop();
        if (!configManager.isUpdateCheckEnabled()) {
            updateAvailable = false;
            return;
        }

        latestVersionUrl = "https://modrinth.com/plugin/" + configManager.getModrinthProjectSlug();
        task = asyncTaskScheduler.scheduleRepeatingAsync(this::checkForUpdates, CHECK_INTERVAL_SECONDS);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getCurrentVersion() {
        return plugin.getDescription().getVersion();
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getLatestVersionUrl() {
        return latestVersionUrl;
    }

    private void checkForUpdates() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.modrinth.com/v2/project/" + configManager.getModrinthProjectSlug() + "/version"))
                    .timeout(Duration.ofSeconds(10L))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                plugin.getLogger().warning("Update check failed: HTTP " + response.statusCode());
                return;
            }

            JsonArray versions = JsonParser.parseString(response.body()).getAsJsonArray();
            if (versions.isEmpty()) {
                return;
            }

            JsonObject latest = versions.get(0).getAsJsonObject();
            String remoteVersion = latest.get("version_number").getAsString();
            String currentVersion = getCurrentVersion();

            if (!remoteVersion.equalsIgnoreCase(currentVersion)) {
                latestVersion = remoteVersion;
                latestVersionUrl = "https://modrinth.com/plugin/" + configManager.getModrinthProjectSlug();
                updateAvailable = true;
                notifyConsole();
            } else {
                updateAvailable = false;
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to check for updates: " + e.getMessage());
        }
    }

    private void notifyConsole() {
        asyncTaskScheduler.runOnMainContext(() -> {
            String message = configManager.getMessage("update_available_console")
                    .replace("%version%", latestVersion)
                    .replace("%url%", latestVersionUrl);
            plugin.getLogger().warning(message);
        });
    }
}
