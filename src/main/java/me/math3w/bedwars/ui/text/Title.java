package me.math3w.bedwars.ui.text;

import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.utils.Utils;

public class Title {
    private final BedwarsPlayer player;
    private UpdatableText title;
    private UpdatableText subtitle;

    public Title(BedwarsPlayer player) {
        this.player = player;
    }

    public Title(BedwarsPlayer player, UpdatableText title, UpdatableText subtitle) {
        this.player = player;
        this.title = title;
        this.subtitle = subtitle;
    }

    public void update() {
        if (!player.isOnline()) return;

        Utils.setTitle(player.getBukkitPlayer(),
                title.replacePlaceholders(player), subtitle.replacePlaceholders(player),
                0, 21, 0);
    }

    public BedwarsPlayer getPlayer() {
        return player;
    }

    public UpdatableText getTitle() {
        return title;
    }

    public void setTitle(UpdatableText title) {
        this.title = title;
    }

    public UpdatableText getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(UpdatableText subtitle) {
        this.subtitle = subtitle;
    }
}
