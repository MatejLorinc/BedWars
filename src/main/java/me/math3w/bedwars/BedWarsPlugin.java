package me.math3w.bedwars;

import me.math3w.bedwars.config.ConfigManager;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.listeners.DisabledEvents;
import me.math3w.bedwars.listeners.Listeners;
import me.math3w.bedwars.listeners.MenuListeners;
import me.math3w.bedwars.shop.ShopUtils;
import me.math3w.bedwars.shop.Trader;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.ChatColor.*;

public class BedWarsPlugin extends JavaPlugin {
    public static final String PREFIX = DARK_GRAY + "[" + RED + "BedWars" + DARK_GRAY + "]";
    public static final String NAME = "" + RED + BOLD + "BedWars";

    private Game game;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new DisabledEvents(), this);
        getServer().getPluginManager().registerEvents(new MenuListeners(), this);

        ConfigManager.init(this);
        game = new Game(this);
        ShopUtils.registerEntity("Villager", 120, Trader.class);
    }

    public Game getGame() {
        return game;
    }
}
