package me.math3w.bedwars.game.state;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.state.tasks.UIUpdateTask;
import me.math3w.bedwars.game.state.tasks.countdowns.StateCountdownTask;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.text.UI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.ChatColor.*;

public abstract class State implements Listener {
    protected final Game game;
    private final Set<BukkitRunnable> tasks = new HashSet<>();
    private StateCountdownTask countdownTask;

    public State(Game game) {
        this.game = game;
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, game.getPlugin());

        if (countdownTask != null) {
            countdownTask.start(game.getPlugin());
            this.addTask(countdownTask);
        }

        BukkitRunnable uiUpdateTask = new UIUpdateTask(game.getPlayerManager());
        uiUpdateTask.runTaskTimer(game.getPlugin(), 1, 20);
        this.addTask(uiUpdateTask);
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
        tasks.forEach(BukkitRunnable::cancel);
    }

    public abstract UI getUi(BedwarsPlayer player);

    public abstract ItemStack[] getInventoryPreset(BedwarsPlayer player);

    public abstract State getNextState();

    public void addTask(BukkitRunnable task) {
        tasks.add(task);
    }

    public void setCountdownTask(StateCountdownTask task) {
        countdownTask = task;
    }

    public int getRemainingSeconds() {
        return this.countdownTask.getRemainingSeconds();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMessageSend(AsyncPlayerChatEvent event) {
        Player bukkitPlayer = event.getPlayer();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer sender = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        String message = event.getMessage();

        if (sender.isSpectating()) {
            event.setFormat(DARK_GRAY + "[" + sender.getColor() + "Spectator" + DARK_GRAY + "] " +
                    GRAY + "%1$s: " + WHITE + "%2$s");
            event.getRecipients().clear();
            game.getPlayerManager().getPlayers().stream().filter(BedwarsPlayer::isSpectating)
                    .forEach(recipient -> event.getRecipients().add(recipient.getBukkitPlayer()));
        } else if (sender.getTeam() == null) {
            event.setFormat(DARK_GRAY + "%1$s: " + GRAY + "%2$s");
            event.getRecipients().clear();
            game.getPlayerManager().getOnlinePlayers().forEach(recipient -> event.getRecipients().add(recipient.getBukkitPlayer()));
        } else {
            String format = DARK_GRAY + "[" + sender.getColor() + (message.startsWith("!") ? "All" : "Team") + DARK_GRAY + "] " +
                    GRAY + "%1$s: " + WHITE + "%2$s";

            if (message.startsWith("!")) {
                event.setMessage(message.substring(1));
            } else {
                event.getRecipients().clear();
                sender.getTeam().getOnlinePlayers().forEach(recipient -> event.getRecipients().add(recipient.getBukkitPlayer()));
            }

            event.setFormat(format);
        }
    }
}
