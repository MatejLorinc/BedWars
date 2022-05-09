package me.math3w.bedwars.items.specialitems.items;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.items.specialitems.SpecialItem;
import me.math3w.bedwars.spawner.Resource;
import me.math3w.bedwars.utils.ItemUtils;
import me.math3w.bedwars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

import static org.bukkit.ChatColor.YELLOW;

public class RandomSpawner extends SpecialItem {
    public RandomSpawner(Game game) {
        super(game);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.SEA_LANTERN);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(YELLOW + "Random spawner");
        item.setItemMeta(itemMeta);
        return item;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRandomSpawnerPlace(BlockPlaceEvent event) {
        if (!this.getItem().isSimilar(event.getPlayer().getItemInHand())) return;

        Block block = event.getBlock();

        block.removeMetadata("playerPlaced", game.getPlugin());

        ArmorStand stand = Utils.spawnStand(block.getLocation().add(0.5, -0.5, 0.5));
        new BukkitRunnable() {
            int timeRemaining = 20;

            @Override
            public void run() {
                Resource resource = Resource.values()[new Random().nextInt(3)];
                ItemUtils.dropItem(block.getLocation().add(0.5, 1, 0.5), resource.getItem());

                if (timeRemaining <= 0) {
                    stand.remove();
                    block.setType(Material.AIR);
                    this.cancel();
                    return;
                }

                stand.setCustomName("" + YELLOW + timeRemaining);

                timeRemaining--;
            }
        }.runTaskTimer(game.getPlugin(), 0, 20);
    }
}
