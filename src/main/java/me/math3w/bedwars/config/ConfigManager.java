package me.math3w.bedwars.config;

import me.math3w.bedwars.BedWarsPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private static ConfigManager instance;

    private final FileConfiguration config;
    private final ConfigFile locationsConfig;
    private final ConfigFile mapsConfig;
    private final ConfigFile sqlConfig;

    public ConfigManager(BedWarsPlugin plugin) {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        locationsConfig = new ConfigFile(plugin, "locations");
        mapsConfig = new ConfigFile(plugin, "maps");
        sqlConfig = new ConfigFile(plugin, "mysql");
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            throw new NullPointerException("Config manager hasn't been initialized");
        }
        return instance;
    }

    public static void init(BedWarsPlugin plugin) {
        instance = new ConfigManager(plugin);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getLocationsConfig() {
        return locationsConfig.getConfig();
    }

    public FileConfiguration getMapsConfig() {
        return mapsConfig.getConfig();
    }

    public FileConfiguration getSqlConfig() {
        return sqlConfig.getConfig();
    }
}
