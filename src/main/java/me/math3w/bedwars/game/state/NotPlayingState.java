package me.math3w.bedwars.game.state;

import me.math3w.bedwars.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public abstract class NotPlayingState extends State {
    public NotPlayingState(Game game) {
        super(game);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
