package me.math3w.bedwars.game;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import me.math3w.bedwars.config.ConfigUtils;
import me.math3w.bedwars.game.team.TeamColor;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.spawner.SpawnerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Map {
    private final ConfigurationSection config;

    private final String name;
    private final Set<BedwarsPlayer> voters = new HashSet<>();

    public Map(ConfigurationSection mapConfig, String name) {
        this.config = mapConfig;

        this.name = name;
    }

    public void load() {
        SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SlimeLoader loader = plugin.getLoader("mysql");

        SlimeWorld.SlimeProperties properties = SlimeWorld.SlimeProperties.builder()
                .difficulty(3)
                .allowAnimals(false)
                .allowMonsters(false)
                .pvp(true)
                .readOnly(true).build();

        try {
            SlimeWorld originalWorld = plugin.loadWorld(loader, name, properties);

            SlimeWorld world = originalWorld.clone("arena");
            plugin.generateWorld(world);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void unload() {
        Bukkit.unloadWorld("arena", false);
    }

    private ConfigurationSection getTeamSection(TeamColor team) {
        return config.getConfigurationSection("teams." + team.name().toLowerCase());
    }

    public Location getSpawn(TeamColor team) {
        return ConfigUtils.deserializeLocation(getTeamSection(team).getConfigurationSection("spawn"), "arena");
    }

    public Location getItemShop(TeamColor team) {
        return ConfigUtils.deserializeLocation(getTeamSection(team).getConfigurationSection("item-shop"), "arena");
    }

    public Set<Location> getBedLocation(TeamColor team) {
        return ConfigUtils.deserializeLocations(getTeamSection(team).getMapList("bed"), "arena");
    }

    public Set<Location> getSpawners(SpawnerType type) {
        return ConfigUtils.deserializeLocations(config.getMapList("spawners." + type.name().toLowerCase()), "arena");
    }

    public String getName() {
        return name;
    }

    public int getVotes() {
        return this.getVoters().size();
    }

    public Set<BedwarsPlayer> getVoters() {
        return this.voters.stream().filter(BedwarsPlayer::isOnline).collect(Collectors.toSet());
    }

    public void addVoter(BedwarsPlayer voter) {
        this.voters.add(voter);
    }

    public void removeVoter(BedwarsPlayer voter) {
        this.voters.remove(voter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Map map = (Map) o;
        return name.equals(map.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
