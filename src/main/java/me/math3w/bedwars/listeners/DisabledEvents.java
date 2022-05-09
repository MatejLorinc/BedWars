package me.math3w.bedwars.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class DisabledEvents implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemCraft(CraftItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onExpChange(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractAtInvisibleStand(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand)) return;

        ArmorStand stand = ((ArmorStand) event.getRightClicked());

        if (stand.isVisible()) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockGrow(BlockGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }
}
