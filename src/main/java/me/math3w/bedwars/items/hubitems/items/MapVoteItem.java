package me.math3w.bedwars.items.hubitems.items;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.Map;
import me.math3w.bedwars.game.state.VotingState;
import me.math3w.bedwars.items.hubitems.HubItem;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.menu.menus.MapVoteMenu;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.RED;

public class MapVoteItem extends HubItem {
    public MapVoteItem(Game game) {
        super(game);
    }

    @Override
    public ItemStack getItem(BedwarsPlayer player) {
        if (!(game.getState() instanceof VotingState)) return null;

        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName("" + RED + BOLD + "Vote map");

        Optional<Map> playerVotedMap = ((VotingState) game.getState()).getPlayerVotedMap(player);
        if (!playerVotedMap.isPresent()) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(itemMeta);
        return item;
    }


    @Override
    public void handleInteract(PlayerInteractEvent event) {
        BedwarsPlayer player = game.getPlayerManager().getOrCreatePlayer(event.getPlayer());
        new MapVoteMenu(game, player).open();
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        BedwarsPlayer player = game.getPlayerManager().getOrCreatePlayer((OfflinePlayer) event.getWhoClicked());
        new MapVoteMenu(game, player).open();
    }
}
