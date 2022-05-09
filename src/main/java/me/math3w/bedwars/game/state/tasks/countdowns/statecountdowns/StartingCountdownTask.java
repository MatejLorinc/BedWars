package me.math3w.bedwars.game.state.tasks.countdowns.statecountdowns;

import me.math3w.bedwars.BedWarsPlugin;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.state.tasks.countdowns.StateCountdownTask;
import me.math3w.bedwars.ui.text.Title;
import org.bukkit.Sound;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;

public class StartingCountdownTask extends StateCountdownTask {
    public StartingCountdownTask(Game game, int duration) {
        super(game, duration);
    }

    @Override
    protected void onSecondElapse() {
        super.onSecondElapse();
        if (this.getRemainingSeconds() <= 5) {
            game.getPlayerManager().getOnlinePlayers().forEach(player -> player.getUi().setTitle(
                    new Title(player, bwPlayer -> "", bwPlayer -> "" + RED + this.getRemainingSeconds())));

            game.getPlayerManager().broadcast(BedWarsPlugin.PREFIX + GRAY + " Game starts in " +
                    RED + this.getRemainingSeconds() + GRAY + " seconds!");

            game.getPlayerManager().getOnlinePlayers().forEach(player -> player.getBukkitPlayer()
                    .playSound(player.getBukkitPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1));
        }
    }

    @Override
    public void onCancel() {
        super.onCancel();
        game.unloadMap();
    }
}
