package me.math3w.bedwars.game.state;

import me.math3w.bedwars.BedWarsPlugin;
import me.math3w.bedwars.config.ConfigManager;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.Map;
import me.math3w.bedwars.game.state.tasks.countdowns.statecountdowns.StartingCountdownTask;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.text.Sidebar;
import me.math3w.bedwars.ui.text.UI;
import me.math3w.bedwars.utils.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

public class StartingState extends PreGameState {
    private final Map map;

    public StartingState(Game game, Map map) {
        super(game);
        this.map = map;
    }

    @Override
    public void onEnable() {
        this.setCountdownTask(new StartingCountdownTask(game, 5));
        super.onEnable();

        game.setMap(map);

        game.getPlayerManager().broadcast(BedWarsPlugin.PREFIX + GRAY + " Voting won map: " + RED + map.getName());
    }

    @Override
    public UI getUi(BedwarsPlayer player) {
        UI ui = super.getUi(player);
        Sidebar sidebar = ui.getSidebar();
        sidebar.setTitle(bwPlayer ->
                "" + RED + BOLD + "Starting " + WHITE + "| " + StringUtils.secondsToTime(this.getRemainingSeconds()));
        return ui;
    }

    @Override
    public ItemStack[] getInventoryPreset(BedwarsPlayer player) {
        ItemStack[] inventory = new ItemStack[36];

        inventory[0] = game.getItemManager().getTeamSelectionItem().getItem(player);

        return inventory;
    }

    @Override
    public State getNextState() {
        return new PlayingState(game);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        if (game.getPlayerManager().getOnlinePlayers().size() - 1 <= ConfigManager.getInstance().getConfig().getInt("max-team-players")) {
            game.setState(new WaitingState(game));
        }
    }
}
