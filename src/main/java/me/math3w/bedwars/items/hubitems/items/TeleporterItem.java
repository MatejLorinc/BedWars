package me.math3w.bedwars.items.hubitems.items;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.items.hubitems.HubItem;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.menu.menus.TeleporterMenu;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class TeleporterItem extends HubItem {
    public TeleporterItem(Game game) {
        super(game);
    }

    @Override
    public ItemStack getItem(BedwarsPlayer player) {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        meta.setDisplayName("" + RED + BOLD + "Teleporter " + GREEN + "(Right Click)");
        lore.add(GRAY + "Click to spectate other players.");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void handleInteract(PlayerInteractEvent event) {
        BedwarsPlayer player = game.getPlayerManager().getOrCreatePlayer(event.getPlayer());
        new TeleporterMenu(game, player).open();
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        BedwarsPlayer player = game.getPlayerManager().getOrCreatePlayer((OfflinePlayer) event.getWhoClicked());
        new TeleporterMenu(game, player).open();
    }
}
