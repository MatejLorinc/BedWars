package me.math3w.bedwars.game.state;

import me.math3w.bedwars.config.ConfigManager;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.player.BedwarsPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class WaitingState extends PreGameState {
    public WaitingState(Game game) {
        super(game);
    }

    @Override
    public ItemStack[] getInventoryPreset(BedwarsPlayer player) {
        ItemStack[] inventory = new ItemStack[36];

        inventory[0] = game.getItemManager().getTeamSelectionItem().getItem(player);

        return inventory;
    }

    @Override
    public State getNextState() {
        return new VotingState(game);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        if (game.getPlayerManager().getPlayers().size() >= ConfigManager.getInstance().getConfig().getInt("players-to-start")) {
            game.setNextState();
        }
    }
}
