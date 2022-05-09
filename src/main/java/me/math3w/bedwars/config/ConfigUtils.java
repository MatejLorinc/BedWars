package me.math3w.bedwars.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.NumberConversions;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigUtils {
    private ConfigUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static Location deserializeLocation(Map<?, ?> location, String worldName) {
        return new Location(
                Bukkit.getWorld(worldName == null ? (String) location.get("world") : worldName),
                NumberConversions.toDouble(location.get("x")),
                NumberConversions.toDouble(location.get("y")),
                NumberConversions.toDouble(location.get("z")),
                location.get("yaw") == null ? 0 : NumberConversions.toFloat(location.get("yaw")),
                location.get("pitch") == null ? 0 : NumberConversions.toFloat(location.get("pitch"))
        );
    }

    public static Location deserializeLocation(ConfigurationSection section, String worldName) {
        return deserializeLocation(section.getValues(false), worldName);
    }

    public static Location deserializeLocation(ConfigurationSection section) {
        return deserializeLocation(section, null);
    }

    public static Set<Location> deserializeLocations(List<Map<?, ?>> locations, String worldName) {
        return locations.stream()
                .map(location -> ConfigUtils.deserializeLocation(location, worldName))
                .collect(Collectors.toSet());
    }
}
