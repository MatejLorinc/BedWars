package me.math3w.bedwars.game.team;

import me.math3w.bedwars.config.ConfigManager;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.player.BedwarsPlayer;
import org.bukkit.Material;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GRAY;

public class Team {
    private final Game game;

    private final TeamColor teamColor;
    private boolean bed = true;

    public Team(Game game, TeamColor teamColor) {
        this.game = game;
        this.teamColor = teamColor;
    }

    public String addPlayer(BedwarsPlayer player) {
        if (this.equals(player.getTeam())) {
            return DARK_GRAY + "You are already connected to this team.";
        }

        if (this.getPlayers().size() >= ConfigManager.getInstance().getConfig().getInt("max-team-players")) {
            return DARK_GRAY + "This team is full.";
        }

        player.setTeam(this);

        return GRAY + "You joined to " + teamColor.getName();
    }

    public Set<BedwarsPlayer> getPlayers() {
        return game.getPlayerManager().getPlayers().stream()
                .filter(player -> this.equals(player.getTeam()))
                .collect(Collectors.toSet());
    }

    public Set<BedwarsPlayer> getOnlinePlayers() {
        return this.getPlayers().stream().filter(BedwarsPlayer::isOnline).collect(Collectors.toSet());
    }

    public int getSize() {
        return Math.toIntExact(this.getOnlinePlayers().stream()
                .filter(player -> !player.isSpectating())
                .count());
    }

    public boolean hasBed() {
        return bed;
    }

    public void breakBed() {
        bed = false;
        game.getMap().getBedLocation(this.getColor())
                .forEach(bedLocation -> {
                    if (!bedLocation.getBlock().getType().equals(Material.BED_BLOCK)) {
                        return;
                    }
                    bedLocation.getBlock().setType(Material.AIR, false);
                });
    }

    public TeamColor getColor() {
        return teamColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return teamColor == team.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor);
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamColor=" + teamColor +
                ", bed=" + bed +
                '}';
    }
}
