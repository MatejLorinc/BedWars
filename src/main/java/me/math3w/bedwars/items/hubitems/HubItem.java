package me.math3w.bedwars.items.hubitems;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.player.BedwarsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class HubItem implements Listener {
    protected final Game game;

    public HubItem(Game game) {
        this.game = game;

        Bukkit.getPluginManager().registerEvents(this, game.getPlugin());
    }

    public abstract ItemStack getItem(BedwarsPlayer player);

    public abstract void handleInteract(PlayerInteractEvent event);

    public abstract void handleClick(InventoryClickEvent event);

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;

        Player bukkitPlayer = event.getPlayer();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        if (this.getItem(player) == null || !event.getItem().isSimilar(this.getItem(player))) return;

        handleInteract(event);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player bukkitPlayer = (Player) event.getWhoClicked();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        ItemStack item = event.getCurrentItem();

        if (this.getItem(player) == null || item == null) return;

        if (this.getItem(player).isSimilar(item)) {
            handleClick(event);
            event.setCancelled(true);
        } else if (event.getHotbarButton() >= 0 && bukkitPlayer.getInventory().getItem(event.getHotbarButton()) != null) {
            ItemStack hotbarItem = bukkitPlayer.getInventory().getItem(event.getHotbarButton());
            if (this.getItem(player).isSimilar(hotbarItem)) {
                handleClick(event);
                event.setCancelled(true);
            }
        }
    }
}
