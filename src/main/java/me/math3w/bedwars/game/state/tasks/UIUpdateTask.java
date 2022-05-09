package me.math3w.bedwars.game.state.tasks;

import me.math3w.bedwars.player.PlayerManager;
import org.bukkit.scheduler.BukkitRunnable;

public class UIUpdateTask extends BukkitRunnable {
    private final PlayerManager playerManager;

    public UIUpdateTask(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public void run() {
        playerManager.updateUI();
    }
}
