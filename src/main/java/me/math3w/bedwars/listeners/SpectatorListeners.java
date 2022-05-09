package me.math3w.bedwars.listeners;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.player.BedwarsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class SpectatorListeners implements Listener {
    private final Game game;

    public SpectatorListeners(Game game) {
        this.game = game;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelItemDrop(PlayerDropItemEvent event) {
        Player bukkitPlayer = event.getPlayer();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        if (player.isSpectating()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelItemPickup(PlayerPickupItemEvent event) {
        Player bukkitPlayer = event.getPlayer();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        if (player.isSpectating()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player bukkitPlayer = (Player) event.getEntity();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        if (player.isSpectating()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player bukkitPlayer = (Player) event.getDamager();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        if (player.isSpectating()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelFoodChange(FoodLevelChangeEvent event) {
        Player bukkitPlayer = (Player) event.getEntity();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        if (player.isSpectating()) event.setCancelled(true);
    }
}
