package me.math3w.bedwars.ui.text;

import me.math3w.bedwars.player.BedwarsPlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Sidebar {
    private final BedwarsPlayer player;
    private Objective sidebarObjective;

    private UpdatableText title;
    private List<Line> lines = new ArrayList<>();

    private Sidebar(Builder builder) {
        this.player = builder.player;
        this.title = builder.title;

        Collections.reverse(builder.lines);
        for (Line.Builder lineBuilder : builder.lines) {
            this.lines.add(lineBuilder.sidebar(this).build());
        }
        Collections.reverse(this.lines);
    }

    public void remove() {
        sidebarObjective.unregister();
    }

    public void update(Scoreboard scoreboard) {
        if (!player.isOnline()) return;

        if (scoreboard.getObjective("sidebar") == null) {
            sidebarObjective = scoreboard.registerNewObjective("sidebar", "dummy");
            sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        updateTitle();
        for (Line line : lines) {
            line.update(scoreboard);
        }
    }

    public void updateTitle() {
        sidebarObjective.setDisplayName(title.replacePlaceholders(player));
    }

    public Objective getObjective() {
        return sidebarObjective;
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

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sidebar sidebar = (Sidebar) o;
        return player.equals(sidebar.player) && title.equals(sidebar.title) && lines.equals(sidebar.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, title, lines);
    }

    public static class Builder {
        private final List<Line.Builder> lines = new ArrayList<>();
        private BedwarsPlayer player;
        private UpdatableText title;

        public Builder player(BedwarsPlayer player) {
            this.player = player;
            return this;
        }

        public Builder title(UpdatableText title) {
            this.title = title;
            return this;
        }

        public Builder addLine(Line.Builder line) {
            lines.add(line);
            return this;
        }

        public Builder addLine(UpdatableText text) {
            Line.Builder line = new Line.Builder().text(text);
            return addLine(line);
        }

        public Builder addLine() {
            return addLine(new Line.Builder());
        }

        public Sidebar build() {
            if (player == null) {
                throw new IllegalStateException("Player is not defined");
            }
            if (title == null) {
                throw new IllegalStateException("Title is not defined");
            }
            if (lines.size() == 0) {
                throw new IllegalStateException("Sidebar cannot be created without lines");
            }

            return new Sidebar(this);
        }
    }
}
