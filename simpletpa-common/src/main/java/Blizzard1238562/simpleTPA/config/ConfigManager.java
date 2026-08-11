package Blizzard1238562.simpleTPA.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration activeConfig;
    private FileConfiguration bundledDefaults;

    private int tpaCooldownSeconds;
    private int tpaRequestTimeoutSeconds;
    private boolean updateCheckEnabled;
    private String modrinthProjectSlug;
    private boolean placeholderApiFormattingEnabled;
    private String playerDisplayFormat;
    private boolean teleportWarmupEnabled;
    private int teleportWarmupSeconds;
    private boolean allowMultipleRequests;
    private boolean usageHintSuppressed;
    private boolean tpaHereEnabled;
    private int tpaHereCooldownSeconds;
    private int tpaHereRequestTimeoutSeconds;
    private boolean tpoEnabled;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (configFile.exists()) {
            plugin.getLogger().info("Loading existing config.yml and applying missing defaults...");
            activeConfig = mergeWithDefaults(configFile);
        } else {
            plugin.saveDefaultConfig();
            activeConfig = plugin.getConfig();
            loadBundledDefaults();
        }
        applySettings();
    }

    public void reload() {
        plugin.reloadConfig();
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        activeConfig = mergeWithDefaults(configFile);
        applySettings();
    }

    private void applySettings() {
        tpaCooldownSeconds = activeConfig.getInt("settings.tpa_cooldown", 30);
        tpaRequestTimeoutSeconds = activeConfig.getInt("settings.tpa_request_timeout", 60);
        updateCheckEnabled = activeConfig.getBoolean("settings.check_for_updates", true);
        modrinthProjectSlug = activeConfig.getString("settings.modrinth_project_slug", "simpletpaplugin");
        placeholderApiFormattingEnabled = activeConfig.getBoolean("settings.use_placeholderapi_formatting", true);
        playerDisplayFormat = activeConfig.getString("settings.player_display_format", "%player%");
        teleportWarmupEnabled = activeConfig.getBoolean("settings.teleport_warmup_enabled", false);
        teleportWarmupSeconds = activeConfig.getInt("settings.teleport_warmup_seconds", 5);
        allowMultipleRequests = activeConfig.getBoolean("settings.allow_multiple_requests", false);
        usageHintSuppressed = activeConfig.getBoolean("settings.suppress_usage_hint", true);
        tpaHereEnabled = activeConfig.getBoolean("settings.tpahere_enabled", true);
        tpaHereCooldownSeconds = activeConfig.getInt("settings.tpahere_cooldown", 30);
        tpaHereRequestTimeoutSeconds = activeConfig.getInt("settings.tpahere_request_timeout", 60);
        tpoEnabled = activeConfig.getBoolean("settings.tpo_enabled", true);
    }

    public int getTpaCooldownSeconds() {
        return tpaCooldownSeconds;
    }

    public int getTpaRequestTimeoutSeconds() {
        return tpaRequestTimeoutSeconds;
    }

    public boolean isUpdateCheckEnabled() {
        return updateCheckEnabled;
    }

    public String getModrinthProjectSlug() {
        return modrinthProjectSlug;
    }

    public boolean isPlaceholderApiFormattingEnabled() {
        return placeholderApiFormattingEnabled;
    }

    public String getPlayerDisplayFormat() {
        return playerDisplayFormat;
    }

    public boolean isTeleportWarmupEnabled() {
        return teleportWarmupEnabled;
    }

    public int getTeleportWarmupSeconds() {
        return teleportWarmupSeconds;
    }

    public boolean isMultipleRequestsAllowed() {
        return allowMultipleRequests;
    }

    public boolean isUsageHintSuppressed() {
        return usageHintSuppressed;
    }

    public boolean isTpaHereEnabled() {
        return tpaHereEnabled;
    }

    public int getTpaHereCooldownSeconds() {
        return tpaHereCooldownSeconds;
    }

    public int getTpaHereRequestTimeoutSeconds() {
        return tpaHereRequestTimeoutSeconds;
    }

    public boolean isTpoEnabled() {
        return tpoEnabled;
    }

    public String getMessage(String key) {
        return getConfigValue("messages." + key, "Missing message: " + key);
    }

    public String getConfigValue(String key, String defaultValue) {
        if (activeConfig != null && activeConfig.contains(key)) {
            return activeConfig.getString(key);
        }
        if (bundledDefaults != null && bundledDefaults.contains(key)) {
            return bundledDefaults.getString(key);
        }
        return defaultValue;
    }

    public Logger getLogger() {
        return plugin.getLogger();
    }

    private void loadBundledDefaults() {
        try (InputStream stream = plugin.getResource("config.yml")) {
            if (stream == null) {
                plugin.getLogger().warning("Could not load bundled config.yml.");
                bundledDefaults = null;
                return;
            }
            bundledDefaults = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            plugin.getLogger().severe("Could not load bundled config.yml: " + e.getMessage());
            bundledDefaults = null;
        }
    }

    private FileConfiguration mergeWithDefaults(File configFile) {
        YamlConfiguration userConfig = new YamlConfiguration();
        try {
            userConfig.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            return recoverFromBrokenConfig(configFile, e);
        }

        try (InputStream stream = plugin.getResource("config.yml")) {
            if (stream == null) {
                plugin.getLogger().warning("Could not load bundled config.yml.");
                return userConfig;
            }

            FileConfiguration defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
            bundledDefaults = defaults;
            Map<String, Object> missingKeys = new LinkedHashMap<>();
            for (String key : defaults.getKeys(true)) {
                if (!defaults.isConfigurationSection(key) && !userConfig.contains(key)) {
                    missingKeys.put(key, defaults.get(key));
                }
            }

            if (missingKeys.isEmpty()) {
                plugin.getLogger().info("config.yml is up to date.");
                return userConfig;
            }

            appendMissingKeys(configFile, missingKeys);
            userConfig = YamlConfiguration.loadConfiguration(configFile);
            plugin.getLogger().info("config.yml updated with " + missingKeys.size() + " new default value(s).");

            for (String key : defaults.getKeys(true)) {
                if (!defaults.isConfigurationSection(key) && !userConfig.contains(key)) {
                    userConfig.set(key, defaults.get(key));
                }
            }
            return userConfig;
        } catch (IOException e) {
            plugin.getLogger().severe("Could not merge config.yml: " + e.getMessage());
            return userConfig;
        }
    }

    private FileConfiguration recoverFromBrokenConfig(File configFile, Exception cause) {
        plugin.getLogger().severe("config.yml could not be parsed and is invalid YAML: " + cause.getMessage());

        File backupFile = new File(configFile.getParentFile(), "config.yml.broken-" + System.currentTimeMillis());
        try {
            Files.copy(configFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            plugin.getLogger().severe("The broken config.yml was backed up to " + backupFile.getName() + ".");
        } catch (IOException backupException) {
            plugin.getLogger().severe("Could not back up the broken config.yml: " + backupException.getMessage());
        }

        try (InputStream stream = plugin.getResource("config.yml")) {
            if (stream == null) {
                plugin.getLogger().severe("Could not load bundled config.yml to restore defaults.");
                return new YamlConfiguration();
            }
            Files.copy(stream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            plugin.getLogger().severe("A fresh default config.yml was written. Please re-apply any customizations from the backup file.");
        } catch (IOException writeException) {
            plugin.getLogger().severe("Could not write a fresh default config.yml: " + writeException.getMessage());
            return new YamlConfiguration();
        }

        loadBundledDefaults();
        return YamlConfiguration.loadConfiguration(configFile);
    }

    private void appendMissingKeys(File configFile, Map<String, Object> missingKeys) throws IOException {
        String defaultRawText;
        try (InputStream stream = plugin.getResource("config.yml")) {
            if (stream == null) {
                return;
            }
            defaultRawText = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }

        List<String> userLines = new ArrayList<>(Arrays.asList(
                new String(Files.readAllBytes(configFile.toPath()), StandardCharsets.UTF_8).split("\\r?\\n", -1)));

        String[] defaultLines = defaultRawText.split("\\r?\\n");
        Map<String, List<String>> missingLinesBySection = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : missingKeys.entrySet()) {
            String key = entry.getKey();
            String rootSection = key.contains(".") ? key.substring(0, key.indexOf('.')) : key;
            String leafKey = key.contains(".") ? key.substring(key.lastIndexOf('.') + 1) : key;

            String line = findDefaultLine(defaultLines, leafKey);
            if (line == null) {
                line = buildFallbackLine(key, leafKey, entry.getValue());
            }
            missingLinesBySection.computeIfAbsent(rootSection, section -> new ArrayList<>()).add(line);
        }

        for (Map.Entry<String, List<String>> sectionEntry : missingLinesBySection.entrySet()) {
            insertLinesUnderSection(userLines, sectionEntry.getKey(), sectionEntry.getValue());
        }

        String merged = String.join(System.lineSeparator(), userLines);
        Files.write(configFile.toPath(), merged.getBytes(StandardCharsets.UTF_8));
    }

    private String findDefaultLine(String[] defaultLines, String leafKey) {
        String searchPrefix = leafKey + ":";
        for (String line : defaultLines) {
            String trimmed = line.trim();
            if (trimmed.startsWith(searchPrefix) && !trimmed.startsWith("#")) {
                return line;
            }
        }
        return null;
    }

    private String buildFallbackLine(String key, String leafKey, Object value) {
        String stringValue = value != null ? value.toString() : "";
        int depth = key.split("\\.").length;
        String indent = "  ".repeat(Math.max(0, depth - 1));
        if (needsQuoting(stringValue)) {
            return indent + leafKey + ": \"" + stringValue + "\"";
        }
        return indent + leafKey + ": " + stringValue;
    }

    private boolean needsQuoting(String value) {
        if (value.startsWith("&") || value.startsWith("*")) {
            return true;
        }
        String specialChars = ":#{}[],?-.!|>\"'@`";
        for (char c : specialChars.toCharArray()) {
            if (value.indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }

    private void insertLinesUnderSection(List<String> userLines, String rootSection, List<String> linesToAdd) {
        int sectionStartLine = -1;
        for (int i = 0; i < userLines.size(); i++) {
            String trimmed = userLines.get(i).trim();
            if (trimmed.equals(rootSection + ":") && !trimmed.startsWith("#")) {
                sectionStartLine = i;
                break;
            }
        }

        if (sectionStartLine < 0) {
            if (!userLines.isEmpty() && !userLines.get(userLines.size() - 1).isEmpty()) {
                userLines.add("");
            }
            userLines.add(rootSection + ":");
            userLines.addAll(linesToAdd);
            return;
        }

        int insertPos = userLines.size();
        for (int i = sectionStartLine + 1; i < userLines.size(); i++) {
            String line = userLines.get(i);
            if (line.isEmpty() || line.startsWith(" ") || line.startsWith("\t") || line.startsWith("#")) {
                continue;
            }
            String trimmed = line.trim();
            boolean looksLikeKey = trimmed.endsWith(":") || (trimmed.contains(":") && !trimmed.startsWith("-"));
            if (looksLikeKey) {
                String potentialKey = trimmed.endsWith(":")
                        ? trimmed.substring(0, trimmed.length() - 1).trim()
                        : trimmed.substring(0, trimmed.indexOf(':')).trim();
                if (!potentialKey.equals(rootSection)) {
                    insertPos = i;
                    break;
                }
            }
        }

        if (insertPos > 0 && !userLines.get(insertPos - 1).isEmpty()) {
            userLines.add(insertPos, "");
            insertPos++;
        }

        for (String line : linesToAdd) {
            userLines.add(insertPos, line);
            insertPos++;
        }
    }
}
