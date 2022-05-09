package me.math3w.bedwars.listeners;

import me.math3w.bedwars.BedWarsPlugin;
import me.math3w.bedwars.game.state.State;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.text.UI;
import me.math3w.bedwars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Listeners implements Listener {
    private final BedWarsPlugin plugin;

    public Listeners(BedWarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        BedwarsPlayer bwPlayer = plugin.getGame().getPlayerManager().getOrCreatePlayer(player);
        State state = plugin.getGame().getState();

        Utils.resetPlayer(player);

        ItemStack[] inventory = state.getInventoryPreset(bwPlayer);
        player.getInventory().setContents(inventory);
        player.updateInventory();

        UI UI = state.getUi(bwPlayer);
        player.setScoreboard(UI.getScoreboard());
        bwPlayer.setUi(UI);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTntPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (block.getType().equals(Material.TNT)) {
            block.removeMetadata("playerPlaced", plugin);
            block.setType(Material.AIR);
            block.getWorld().spawn(block.getLocation().add(0.5, 0.5, 0.5), TNTPrimed.class);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onExplode(EntityExplodeEvent event) {
        List<Block> blockList = new ArrayList<>(event.blockList());

        for (Block destroyedBlock : blockList) {
            if (destroyedBlock.hasMetadata("playerPlaced")) {
                destroyedBlock.removeMetadata("playerPlaced", plugin);
                continue;
            }

            event.blockList().remove(destroyedBlock);
        }
    }
}
