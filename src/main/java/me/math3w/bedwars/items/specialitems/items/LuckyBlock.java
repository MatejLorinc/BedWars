package me.math3w.bedwars.items.specialitems.items;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.items.specialitems.SpecialItem;
import me.math3w.bedwars.shop.Category;
import me.math3w.bedwars.shop.ShopItem;
import me.math3w.bedwars.utils.ItemUtils;
import me.math3w.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.YELLOW;

public class LuckyBlock extends SpecialItem {
    public LuckyBlock(Game game) {
        super(game);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = ItemUtils.createSkull("4b92cb43333aa621c70eef4ebf299ba412b446fe12e341ccc582f3192189");
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(YELLOW + "Lucky block");
        item.setItemMeta(itemMeta);
        return item;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLuckyBlockPlace(BlockPlaceEvent event) {
        if (!this.getItem().isSimilar(event.getPlayer().getItemInHand()))
            return;

        event.getBlock().removeMetadata("playerPlaced", game.getPlugin());

        ArmorStand stand = Utils.spawnStand(event.getBlock().getLocation().add(0.5, -1, 0.5));

        List<ItemStack> possibleDrops = Arrays.stream(Category.values())
                .flatMap(category -> Arrays.stream(category.getItems(game, game.getPlayerManager().getOrCreatePlayer(event.getPlayer()))))
                .map(ShopItem::getItem)
                .collect(Collectors.toList());
        int dropIndex = new Random().nextInt(Math.toIntExact(possibleDrops.size()));
        ItemStack drop = possibleDrops.get(dropIndex);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new BukkitRunnable() {
            int timeRemaining = 3;

            @Override
            public void run() {
                if (timeRemaining <= 0) {
                    stand.remove();
                    event.getBlock().setType(Material.AIR);

                    ItemUtils.dropItem(event.getBlock().getLocation().add(0.5, 0, 0.5), drop);

                    this.cancel();
                    return;
                }

                stand.setCustomName("" + YELLOW + timeRemaining);

                timeRemaining--;
            }
        }, 0, 20);
    }
}
