package me.math3w.bedwars.game.team;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public enum TeamColor {
    RED(ChatColor.RED, DyeColor.RED),
    YELLOW(ChatColor.YELLOW, DyeColor.YELLOW),
    GREEN(ChatColor.GREEN, DyeColor.LIME),
    BLUE(ChatColor.BLUE, DyeColor.BLUE);

    private final ChatColor chatColor;
    private final DyeColor dyeColor;

    TeamColor(ChatColor chatColor, DyeColor dyeColor) {
        this.chatColor = chatColor;
        this.dyeColor = dyeColor;
    }

    public String getRawName() {
        return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
    }

    public String getName() {
        return chatColor + this.getRawName();
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }
}
