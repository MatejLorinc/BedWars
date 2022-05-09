package me.math3w.bedwars.items.specialitems.items;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.team.TeamColor;
import me.math3w.bedwars.items.specialitems.SpecialItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import static org.bukkit.ChatColor.YELLOW;

public class Mine extends SpecialItem {
    public Mine(Game game) {
        super(game);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.STRING);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(YELLOW + "Mine");
        item.setItemMeta(itemMeta);
        return item;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinePlace(BlockPlaceEvent event) {
        if (!this.getItem().isSimilar(event.getPlayer().getItemInHand())) return;

        Block block = event.getBlock();

        block.removeMetadata("playerPlaced", game.getPlugin());
        TeamColor minePlacerTeam = game.getPlayerManager().getOrCreatePlayer(event.getPlayer()).getTeam().getColor();
        block.setMetadata("mine", new FixedMetadataValue(game.getPlugin(), minePlacerTeam.name()));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMineBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (!block.hasMetadata("mine")) return;

        event.setCancelled(true);
        block.removeMetadata("mine", game.getPlugin());
        block.setType(Material.AIR);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMineStep(PlayerMoveEvent event) {
        Player bukkitPlayer = event.getPlayer();
        Block block = bukkitPlayer.getLocation().getBlock();
        if (!block.hasMetadata("mine")) return;

        String minePlacer = block.getMetadata("mine").get(0).asString();
        String playerTeam = game.getPlayerManager().getOrCreatePlayer(bukkitPlayer).getTeam().getColor().name();
        if (minePlacer.equals(playerTeam)) return;

        block.removeMetadata("mine", game.getPlugin());
        block.setType(Material.AIR);
        bukkitPlayer.damage(5);
    }
}
