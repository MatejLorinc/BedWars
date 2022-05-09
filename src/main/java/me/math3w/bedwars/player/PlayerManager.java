package me.math3w.bedwars.player;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.statistics.DatabaseManager;
import me.math3w.bedwars.statistics.Statistic;
import me.math3w.bedwars.ui.text.UI;
import org.bukkit.OfflinePlayer;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlayerManager {
    private final Game game;
    private final Set<BedwarsPlayer> players = new HashSet<>();

    public PlayerManager(Game game) {
        this.game = game;
    }

    public void addPlayer(BedwarsPlayer player) {
        players.add(player);
    }

    public void removePlayer(UUID uuid) {
        if (!getBedwarsPlayer(uuid).isPresent()) return;

        players.removeIf((bwPlayer) -> bwPlayer.getUniqueId().equals(uuid));
    }

    public Set<BedwarsPlayer> getPlayers() {
        return players;
    }

    public Set<BedwarsPlayer> getOnlinePlayers() {
        return this.getPlayers().stream().filter(BedwarsPlayer::isOnline).collect(Collectors.toSet());
    }

    public Optional<BedwarsPlayer> getBedwarsPlayer(UUID uuid) {
        return this.getPlayers().stream()
                .filter(bwPlayer -> bwPlayer.getUniqueId().equals(uuid))
                .findAny();
    }

    public Optional<BedwarsPlayer> getBedwarsPlayer(OfflinePlayer player) {
        return this.getBedwarsPlayer(player.getUniqueId());
    }

    public BedwarsPlayer getOrCreatePlayer(UUID uuid) {
        return this.getBedwarsPlayer(uuid).orElseGet(() -> {
            BedwarsPlayer player = new BedwarsPlayer(game, uuid);
            addPlayer(player);
            return player;
        });
    }

    public BedwarsPlayer getOrCreatePlayer(OfflinePlayer player) {
        return this.getOrCreatePlayer(player.getUniqueId());
    }

    public void updateUI() {
        for (BedwarsPlayer player : players) {
            UI ui = player.getUi();
            ui.update();
        }
    }

    public void broadcast(Function<BedwarsPlayer, String> message) {
        this.getOnlinePlayers().forEach(player -> player.getBukkitPlayer().sendMessage(message.apply(player)));
    }

    public void broadcast(String message) {
        this.broadcast(player -> message);
    }

    public void addStatistic(BedwarsPlayer player, Statistic statistic) {
        player.addStatistic(statistic);
        DatabaseManager.getInstance().addStatistic(player.getUniqueId(), statistic);
    }
}
