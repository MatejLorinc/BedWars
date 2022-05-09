package me.math3w.bedwars.shop;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.menu.MenuItem;

public class CategoryItem extends MenuItem {
    public CategoryItem(Game game, Category category, BedwarsPlayer customer) {
        super(category.getItem());
        this.addClickAction((event) -> new ShopMenu(game, customer, category).open());
    }
}
