package me.math3w.bedwars.game.state.tasks.countdowns;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CountdownTask extends BukkitRunnable {
    private int remainingSeconds;

    public CountdownTask(int duration) {
        this.remainingSeconds = duration + 1;
    }

    @Override
    public void run() {
        remainingSeconds--;

        if (remainingSeconds == 0) {
            this.onFinish();
            return;
        }

        this.onSecondElapse();
    }

    @Override
    public void cancel() {
        super.cancel();
        if (remainingSeconds > 0) {
            this.onCancel();
        }
    }

    public void start(Plugin plugin) {
        this.runTaskTimer(plugin, 0, 20);
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    protected void onSecondElapse() {
    }

    protected void onFinish() {
        super.cancel();
    }

    protected void onCancel() {
    }
}
