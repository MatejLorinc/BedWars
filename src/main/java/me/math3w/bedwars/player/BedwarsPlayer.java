package me.math3w.bedwars.player;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.team.Team;
import me.math3w.bedwars.statistics.Statistic;
import me.math3w.bedwars.ui.text.UI;
import me.math3w.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.ChatColor.WHITE;

public class BedwarsPlayer {
    private final Game game;

    private final UUID uuid;
    private final java.util.Map<Statistic, Integer> statistics = new HashMap<>();
    private UI ui;
    private Team team;
    private boolean spectating = false;
    private java.util.Map.Entry<BedwarsPlayer, Long> lastDamager;

    public BedwarsPlayer(Game game, UUID uuid) {
        this.game = game;
        this.uuid = uuid;
    }

    public String getName() {
        return this.getColor() + getOfflinePlayer().getName();
    }

    public ChatColor getColor() {
        return team == null ? WHITE : team.getColor().getChatColor();
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean isOnline() {
        return getOfflinePlayer().isOnline();
    }

    public boolean isSpectating() {
        return spectating;
    }

    public void setSpectator() {
        spectating = true;

        Bukkit.getScheduler().scheduleSyncDelayedTask(game.getPlugin(), () -> {
            if (this.getBukkitPlayer() == null) return;

            Utils.resetPlayer(this.getBukkitPlayer());
            this.getBukkitPlayer().setGameMode(GameMode.ADVENTURE);

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (this.getBukkitPlayer().equals(player)) continue;

                player.hidePlayer(this.getBukkitPlayer());
            }

            this.getBukkitPlayer().spigot().setCollidesWithEntities(false);
            this.getBukkitPlayer().setAllowFlight(true);
            this.getBukkitPlayer().setFlying(true);

            this.getBukkitPlayer().getInventory().setItem(0, game.getItemManager().getTeleporterItem().getItem(this));
        }, 2);
    }

    public UI getUi() {
        return ui;
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getStatistic(Statistic statistic) {
        return statistics.getOrDefault(statistic, 0);
    }

    protected void addStatistic(Statistic statistic) {
        statistics.put(statistic, getStatistic(statistic) + 1);
    }

    public BedwarsPlayer getLastDamager() {
        return lastDamager == null ? null : lastDamager.getKey();
    }

    public void setLastDamager(BedwarsPlayer damager) {
        this.lastDamager = new AbstractMap.SimpleEntry<>(damager, System.currentTimeMillis());
    }

    public boolean isLastHitRelevant() {
        return this.getLastDamager() != null && System.currentTimeMillis() - lastDamager.getValue() < 15000;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BedwarsPlayer player = (BedwarsPlayer) o;
        return uuid.equals(player.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
