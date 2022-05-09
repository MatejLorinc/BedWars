package me.math3w.bedwars.ui.menu.menus;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.menu.ClickAction;
import me.math3w.bedwars.ui.menu.Menu;
import me.math3w.bedwars.ui.menu.MenuItem;
import me.math3w.bedwars.ui.menu.actions.CloseInventoryAction;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class TeleporterMenu extends Menu {
    private final Game game;

    public TeleporterMenu(Game game, BedwarsPlayer player) {
        super(player, game.getPlayerManager().getOnlinePlayers().size() / 9 + 1);
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return "" + RED + BOLD + "Teleport to player";
    }

    @Override
    protected void setMenuItems() {
        BedwarsPlayer[] players = game.getPlayerManager().getOnlinePlayers().stream()
                .filter(player -> !player.isSpectating())
                .toArray(BedwarsPlayer[]::new);

        for (int i = 0; i < players.length; i++) {
            BedwarsPlayer player = players[i];

            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();

            meta.setOwner(player.getName());
            meta.setDisplayName(player.getName());
            lore.add(GRAY + "Click to spectate!");

            meta.setLore(lore);
            itemStack.setItemMeta(meta);

            ClickAction teleport = (event) -> this.player.getBukkitPlayer().teleport(player.getBukkitPlayer());
            ClickAction closeInventory =
                    new CloseInventoryAction(() -> game.getState().getInventoryPreset(player), player.getBukkitPlayer());

            MenuItem item = new MenuItem(itemStack)
                    .addClickAction(teleport)
                    .addClickAction(closeInventory);

            this.setItem(i, item);
        }
    }
}
