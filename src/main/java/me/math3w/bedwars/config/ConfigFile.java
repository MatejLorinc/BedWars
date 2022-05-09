package me.math3w.bedwars.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigFile {
    private final File file;
    private final FileConfiguration config;

    public ConfigFile(Plugin plugin, String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName + ".yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName + ".yml", false);
        }

        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        this.file = configFile;
        this.config = config;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
