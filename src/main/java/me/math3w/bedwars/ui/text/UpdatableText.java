package me.math3w.bedwars.ui.text;

import me.math3w.bedwars.player.BedwarsPlayer;

@FunctionalInterface
public interface UpdatableText {
    String replacePlaceholders(BedwarsPlayer player);
}
