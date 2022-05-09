package me.math3w.bedwars.game.state;

import me.math3w.bedwars.BedWarsPlugin;
import me.math3w.bedwars.config.ConfigManager;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.Map;
import me.math3w.bedwars.game.state.tasks.countdowns.statecountdowns.VoteCountdownTask;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.menu.menus.MapVoteMenu;
import me.math3w.bedwars.ui.text.Sidebar;
import me.math3w.bedwars.ui.text.UI;
import me.math3w.bedwars.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

public class VotingState extends PreGameState {
    private Set<Map> maps;

    public VotingState(Game game) {
        super(game);
    }

    @Override
    public void onEnable() {
        this.setCountdownTask(new VoteCountdownTask(game, 30));
        super.onEnable();

        ConfigurationSection mapsSection = ConfigManager.getInstance().getMapsConfig().getConfigurationSection("maps");
        maps = mapsSection.getKeys(false).stream()
                .map(mapName -> new Map(mapsSection.getConfigurationSection(mapName), mapName))
                .collect(Collectors.toSet());

        game.getPlayerManager().broadcast(BedWarsPlugin.PREFIX + GRAY + " Map voting has begun!");
    }

    @Override
    public void onDisable() {
        super.onDisable();

        for (BedwarsPlayer player : game.getPlayerManager().getOnlinePlayers()) {
            Player bukkitPlayer = player.getBukkitPlayer();

            if (bukkitPlayer.getOpenInventory() != null &&
                    bukkitPlayer.getOpenInventory().getTopInventory() != null &&
                    bukkitPlayer.getOpenInventory().getTopInventory().getName()
                            .equals(new MapVoteMenu(game, player).getMenuName())) {
                bukkitPlayer.closeInventory();
            }
        }
    }

    @Override
    public UI getUi(BedwarsPlayer player) {
        UI ui = super.getUi(player);
        Sidebar sidebar = ui.getSidebar();
        sidebar.setTitle(bwPlayer -> "" + RED + BOLD + "Voting " + WHITE + "| " + StringUtils.secondsToTime(this.getRemainingSeconds()));
        return ui;
    }

    @Override
    public ItemStack[] getInventoryPreset(BedwarsPlayer player) {
        ItemStack[] inventory = new ItemStack[36];

        inventory[0] = game.getItemManager().getTeamSelectionItem().getItem(player);
        inventory[1] = game.getItemManager().getMapVoteItem().getItem(player);

        return inventory;
    }

    @Override
    public State getNextState() {
        Map mostVotedMap = maps.stream()
                .max(Comparator.comparingInt(Map::getVotes))
                .orElseThrow(() -> new NullPointerException("There are no registered maps"));
        return new StartingState(game, mostVotedMap);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        if (game.getPlayerManager().getOnlinePlayers().size() - 1 < ConfigManager.getInstance().getConfig().getInt("players-to-start")) {
            game.setState(new WaitingState(game));
        }
    }

    public Set<Map> getMaps() {
        return maps;
    }

    public Optional<Map> getPlayerVotedMap(BedwarsPlayer player) {
        return maps.stream().filter(map -> map.getVoters().contains(player)).findAny();
    }

    public void voteMap(BedwarsPlayer player, Map map) {
        this.getPlayerVotedMap(player).ifPresent(votedMap -> votedMap.removeVoter(player)); //Removes previous vote

        if (map != null) {
            map.addVoter(player);
        }
    }
}
