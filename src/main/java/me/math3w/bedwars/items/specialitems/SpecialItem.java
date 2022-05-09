package me.math3w.bedwars.items.specialitems;

import me.math3w.bedwars.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class SpecialItem implements Listener {
    protected final Game game;

    public SpecialItem(Game game) {
        this.game = game;

        Bukkit.getPluginManager().registerEvents(this, game.getPlugin());
    }

    public abstract ItemStack getItem();
}
