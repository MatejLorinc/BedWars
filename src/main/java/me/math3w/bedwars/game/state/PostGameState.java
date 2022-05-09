package me.math3w.bedwars.game.state;

import me.math3w.bedwars.BedWarsPlugin;
import me.math3w.bedwars.config.ConfigManager;
import me.math3w.bedwars.config.ConfigUtils;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.state.tasks.countdowns.statecountdowns.EndCountdownTask;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.statistics.Database;
import me.math3w.bedwars.statistics.DatabaseManager;
import me.math3w.bedwars.statistics.Statistic;
import me.math3w.bedwars.ui.text.Sidebar;
import me.math3w.bedwars.ui.text.UI;
import me.math3w.bedwars.ui.text.UpdatableText;
import me.math3w.bedwars.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

public class PostGameState extends NotPlayingState {
    public PostGameState(Game game) {
        super(game);
    }

    @Override
    public void onEnable() {
        this.setCountdownTask(new EndCountdownTask(game, 15));
        super.onEnable();

        for (BedwarsPlayer player : game.getPlayerManager().getOnlinePlayers()) {
            Player bukkitPlayer = player.getBukkitPlayer();

            Location lobby = ConfigUtils.deserializeLocation(
                    ConfigManager.getInstance().getLocationsConfig().getConfigurationSection("ending-lobby"));
            bukkitPlayer.teleport(lobby);
        }

        List<BedwarsPlayer> topKillers = game.getPlayerManager().getPlayers().stream()
                .sorted((player, otherPlayer) -> Integer.compare(otherPlayer.getStatistic(Statistic.KILLS), player.getStatistic(Statistic.KILLS)))
                .limit(3)
                .collect(Collectors.toList());

        game.getPlayerManager().broadcast(WHITE + "■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        if (game.getAliveTeams().size() == 1) {
            game.getAliveTeams().stream().findAny()
                    .ifPresent(team -> {
                        game.getPlayerManager().broadcast(GRAY + " Winner: " + team.getColor().getName());
                        String players = team.getPlayers().stream()
                                .map(bwPlayer -> GRAY + bwPlayer.getBukkitPlayer().getName())
                                .collect(Collectors.joining(", "));
                        game.getPlayerManager().broadcast("  " + team.getColor().getName() + GRAY + " - " + players);
                    });
        } else {
            game.getPlayerManager().broadcast(" " + GRAY + BOLD + "Draw");
        }
        game.getPlayerManager().broadcast("");
        for (int i = 0; i < topKillers.size(); i++) {
            BedwarsPlayer player = topKillers.get(i);
            game.getPlayerManager().broadcast(" " + RED + BOLD + (i + 1) + ". Killer" + GRAY + " - " +
                    player.getName() + GRAY + " (" + player.getStatistic(Statistic.KILLS) + ")");
        }
        game.getPlayerManager().broadcast(WHITE + "■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
    }

    @Override
    public UI getUi(BedwarsPlayer player) {
        Database stats = DatabaseManager.getInstance().getStatisticsDatabase();
        Function<Statistic, UpdatableText> statLine = statistic ->
                bwPlayer -> "" + WHITE + BOLD + StringUtils.toCamelCaseWithSpace(statistic.name()) + ": " +
                        GRAY + stats.get(bwPlayer.getUniqueId(), statistic);

        UI ui = new UI(player);
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Sidebar.Builder sidebarBuilder = new Sidebar.Builder().player(player)
                .title(bwPlayer -> BedWarsPlugin.NAME)
                .addLine(bwPlayer -> "" + RED + BOLD + "Stats:");

        for (Statistic statistic : Statistic.values()) {
            sidebarBuilder.addLine(statLine.apply(statistic));
        }

        Sidebar sidebar = sidebarBuilder.build();

        ui.setScoreboard(scoreboard);
        ui.setSidebar(sidebar);
        return ui;
    }

    @Override
    public ItemStack[] getInventoryPreset(BedwarsPlayer player) {
        return new ItemStack[36];
    }

    @Override
    public State getNextState() {
        return null;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, RED + "The game has ended");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
}
