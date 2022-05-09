package me.math3w.bedwars.shop;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.menu.Menu;
import me.math3w.bedwars.ui.menu.MenuItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

import static org.bukkit.ChatColor.*;

public class ShopMenu extends Menu {
    private final Game game;
    private final Category category;

    public ShopMenu(Game game, BedwarsPlayer player, Category category) {
        super(player, 6);
        this.game = game;
        this.category = category;
    }

    @Override
    public String getMenuName() {
        return "" + RED + BOLD + category.getName();
    }

    @Override
    protected void setMenuItems() {
        for (int i = 0; i < 9; i++) {
            ItemStack splitter = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());

            if (i < Category.values().length) {
                Category category = Category.values()[i];
                this.setItem(i, new CategoryItem(game, category, player));

                if (this.category.equals(category)) {
                    splitter = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GREEN.getData());
                }
            }

            ItemMeta splitterMeta = splitter.getItemMeta();
            splitterMeta.setDisplayName(DARK_GRAY + "⬆ Categories");
            splitterMeta.setLore(Collections.singletonList(DARK_GRAY + "⬇ Items"));
            splitter.setItemMeta(splitterMeta);

            this.setItem(i + 9, new MenuItem(splitter));
        }

        int index = 19;
        for (ShopItem item : category.getItems(game, player)) {
            if (index > 52) {
                throw new ArrayIndexOutOfBoundsException("Too many items in shop, category: " + category.getName());
            }

            this.setItem(index, item);

            if ((index - 7) % 9 == 0) {
                index += 3;
            } else {
                index++;
            }
        }
    }
}
