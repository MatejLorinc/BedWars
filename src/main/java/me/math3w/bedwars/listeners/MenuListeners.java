package me.math3w.bedwars.listeners;

import me.math3w.bedwars.ui.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class MenuListeners implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMenuClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Inventory inventory = event.getView().getTopInventory();
        ItemStack item = event.getCurrentItem();

        if (inventory == null || item == null) return;

        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof Menu)) return;

        event.setCancelled(true);

        if (event.getClickedInventory() != inventory) return;

        Menu menu = (Menu) holder;
        menu.handleMenu(event);
    }
}
