package me.math3w.bedwars.ui.text;

import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.player.PlayerManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;

public class Tablist {
    private final PlayerManager playerManager;

    private final BedwarsPlayer player;
    private UpdatableText prefix;
    private UpdatableText suffix;

    public Tablist(PlayerManager playerManager, BedwarsPlayer player) {
        this.playerManager = playerManager;
        this.player = player;
    }

    public void update(Scoreboard scoreboard) {
        for (BedwarsPlayer otherPlayer : playerManager.getOnlinePlayers()) {
            OfflinePlayer otherBukkitPlayer = otherPlayer.getOfflinePlayer();

            if (scoreboard.getTeam(otherBukkitPlayer.getName()) == null) {
                scoreboard.registerNewTeam(otherBukkitPlayer.getName());
                scoreboard.getTeam(otherBukkitPlayer.getName()).addPlayer(otherBukkitPlayer);
            }

            if (prefix != null) {
                scoreboard.getTeam(otherBukkitPlayer.getName())
                        .setPrefix(prefix.replacePlaceholders(otherPlayer));
            }
            if (suffix != null) {
                scoreboard.getTeam(otherBukkitPlayer.getName())
                        .setSuffix(prefix.replacePlaceholders(otherPlayer));
            }
        }
    }

    public BedwarsPlayer getPlayer() {
        return player;
    }

    public UpdatableText getPrefix() {
        return prefix;
    }

    public void setPrefix(UpdatableText prefix) {
        this.prefix = prefix;
    }

    public UpdatableText getSuffix() {
        return suffix;
    }

    public void setSuffix(UpdatableText suffix) {
        this.suffix = suffix;
    }
}
