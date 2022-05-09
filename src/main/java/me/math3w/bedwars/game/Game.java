package me.math3w.bedwars.game;

import me.math3w.bedwars.BedWarsPlugin;
import me.math3w.bedwars.game.state.PlayingState;
import me.math3w.bedwars.game.state.State;
import me.math3w.bedwars.game.state.WaitingState;
import me.math3w.bedwars.game.team.Team;
import me.math3w.bedwars.game.team.TeamColor;
import me.math3w.bedwars.items.ItemManager;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.player.PlayerManager;
import me.math3w.bedwars.statistics.Statistic;
import me.math3w.bedwars.ui.text.UI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

public class Game {
    private final BedWarsPlugin plugin;

    private final ItemManager itemManager;
    private final PlayerManager playerManager;
    private final List<Team> teams;
    private State state;
    private Map map;
    private boolean firstBlood = false;

    public Game(BedWarsPlugin plugin) {
        this.plugin = plugin;

        itemManager = new ItemManager(this);
        playerManager = new PlayerManager(this);
        teams = Arrays.stream(TeamColor.values())
                .map(teamColor -> new Team(this, teamColor))
                .collect(Collectors.toList());

        setState(new WaitingState(this));
    }

    public BedWarsPlugin getPlugin() {
        return plugin;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        if (this.map != null) {
            throw new IllegalStateException("Previous map was not unloaded");
        }

        Bukkit.getConsoleSender().sendMessage(map.getName());
        this.map = map;
        map.load();
    }

    public void unloadMap() {
        if (state instanceof PlayingState) {
            throw new IllegalStateException("Cannot unload map while playing");
        }

        map.unload();
        map = null;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (state == null || (this.state != null && this.state.getClass() == state.getClass())) return;

        if (this.state != null) {
            this.state.onDisable();
        }

        this.state = state;
        state.onEnable();

        for (BedwarsPlayer player : this.getPlayerManager().getOnlinePlayers()) {
            Player bukkitPlayer = player.getBukkitPlayer();

            UI ui = state.getUi(player);
            bukkitPlayer.setScoreboard(ui.getScoreboard());
            player.setUi(ui);

            ItemStack[] inventory = state.getInventoryPreset(player);
            bukkitPlayer.getInventory().setContents(inventory);
            bukkitPlayer.updateInventory();
        }
    }

    public void setNextState() {
        this.setState(this.getState().getNextState());
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Set<Team> getAliveTeams() {
        return teams.stream()
                .filter(team -> team.getSize() > 0)
                .collect(Collectors.toSet());
    }

    public Set<Team> getDeadTeams() {
        return teams.stream()
                .filter(team -> team.getSize() == 0)
                .collect(Collectors.toSet());
    }

    public boolean wasFirstBlood() {
        return firstBlood;
    }

    public void makeFirstBlood(BedwarsPlayer killer) {
        this.getPlayerManager().broadcast(killer.getName() + GRAY + " drew" +
                DARK_RED + BOLD + " FIRST BLOOD" + GRAY + " in this game.");

        this.getPlayerManager().addStatistic(killer, Statistic.FIRST_BLOODS);

        this.firstBlood = true;
    }
}
