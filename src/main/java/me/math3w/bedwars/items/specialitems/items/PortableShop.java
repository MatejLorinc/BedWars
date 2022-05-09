package me.math3w.bedwars.items.specialitems.items;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.items.specialitems.SpecialItem;
import me.math3w.bedwars.shop.ShopUtils;
import me.math3w.bedwars.shop.Trader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.ChatColor.YELLOW;

public class PortableShop extends SpecialItem {
    public PortableShop(Game game) {
        super(game);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.ARMOR_STAND);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(YELLOW + "Portable shop");
        item.setItemMeta(itemMeta);
        return item;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlace(PlayerInteractEvent event) {
        if (!this.getItem().isSimilar(event.getItem())) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        event.setCancelled(true);

        ItemStack item = event.getPlayer().getItemInHand();

        if (item == null) return;

        if (item.getAmount() == 1) {
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        Location location = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);
        Trader trader = ShopUtils.spawnTrader(game, location);

        new BukkitRunnable() {
            int timeRemaining = 15;

            @Override
            public void run() {
                if (timeRemaining <= 0) {
                    trader.getBukkitEntity().remove();
                    this.cancel();
                    return;
                }

                trader.setCustomName("" + YELLOW + timeRemaining);

                timeRemaining--;
            }
        }.runTaskTimer(game.getPlugin(), 0, 20);
    }
}
