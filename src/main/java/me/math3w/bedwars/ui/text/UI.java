package me.math3w.bedwars.ui.text;

import me.math3w.bedwars.player.BedwarsPlayer;
import org.bukkit.scoreboard.Scoreboard;

public class UI {
    private final BedwarsPlayer player;
    private Title title;
    private Actionbar actionbar;
    private Scoreboard scoreboard;
    private Sidebar sidebar;
    private Tablist tablist;

    public UI(BedwarsPlayer player) {
        this.player = player;
    }

    public void update() {
        if (!player.isOnline()) return;

        if (title != null) {
            title.update();
        }
        if (actionbar != null) {
            actionbar.update();
        }
        if (sidebar != null) {
            sidebar.update(scoreboard);
        }
        if (tablist != null) {
            tablist.update(scoreboard);
        }
    }

    public BedwarsPlayer getPlayer() {
        return player;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Actionbar getActionbar() {
        return actionbar;
    }

    public void setActionbar(Actionbar actionbar) {
        this.actionbar = actionbar;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public Sidebar getSidebar() {
        return sidebar;
    }

    public void setSidebar(Sidebar sidebar) {
        this.sidebar = sidebar;
    }

    public Tablist getTablist() {
        return tablist;
    }

    public void setTablist(Tablist tablist) {
        this.tablist = tablist;
    }
}
