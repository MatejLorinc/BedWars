package me.math3w.bedwars.game.state.tasks.countdowns.statecountdowns;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.state.tasks.countdowns.StateCountdownTask;
import org.bukkit.Bukkit;

public class EndCountdownTask extends StateCountdownTask {
    public EndCountdownTask(Game game, int duration) {
        super(game, duration);
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        Bukkit.getServer().spigot().restart();
    }
}
