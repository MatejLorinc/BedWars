package me.math3w.bedwars.game.state;

import me.math3w.bedwars.BedWarsPlugin;
import me.math3w.bedwars.config.ConfigManager;
import me.math3w.bedwars.config.ConfigUtils;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.team.Team;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.statistics.Database;
import me.math3w.bedwars.statistics.DatabaseManager;
import me.math3w.bedwars.statistics.Statistic;
import me.math3w.bedwars.ui.text.*;
import me.math3w.bedwars.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.function.Function;

import static org.bukkit.ChatColor.*;

public abstract class PreGameState extends NotPlayingState {
    public PreGameState(Game game) {
        super(game);
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
                .addLine(bwPlayer -> GRAY + "Players: " + WHITE +
                        game.getPlayerManager().getPlayers().size() + "/" + Bukkit.getMaxPlayers())
                .addLine()
                .addLine(bwPlayer -> "" + RED + BOLD + "Stats:");

        for (Statistic statistic : Statistic.values()) {
            sidebarBuilder.addLine(statLine.apply(statistic));
        }

        Sidebar sidebar = sidebarBuilder.build();

        ui.setScoreboard(scoreboard);
        ui.setSidebar(sidebar);

        ui.setActionbar(new Actionbar(player, bwPlayer -> {
            Team team = bwPlayer.getTeam();
            if (team == null) return "";

            return "" + team.getColor().getChatColor() + BOLD + "■■■■■■■■■■ " + team.getColor().getRawName() + " Team ■■■■■■■■■■";
        }));

        Tablist tablist = new Tablist(game.getPlayerManager(), player);
        tablist.setPrefix(bwPlayer -> bwPlayer.getTeam() == null ? "" + GRAY : "" + bwPlayer.getColor());
        ui.setTablist(tablist);

        return ui;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPregameJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Location lobby = ConfigUtils.deserializeLocation(
                ConfigManager.getInstance().getLocationsConfig().getConfigurationSection("waiting-lobby"));
        player.teleport(lobby);

        event.setJoinMessage(RED + player.getName() + GRAY + " has joined. " +
                RED + "(" + game.getPlayerManager().getPlayers().size() + ")");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPregameQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        game.getPlayerManager().removePlayer(player.getUniqueId());

        event.setQuitMessage(RED + player.getName() + GRAY + " has left. " +
                RED + "(" + game.getPlayerManager().getPlayers().size() + ")");
    }
}
