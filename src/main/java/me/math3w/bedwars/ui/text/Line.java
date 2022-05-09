package me.math3w.bedwars.ui.text;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

import static org.bukkit.ChatColor.*;

public class Line {
    private final Sidebar sidebar;
    private final UpdatableText text;
    private final int score;

    private Team team;

    private Line(Builder builder) {
        this.sidebar = builder.sidebar;
        this.text = builder.text;
        this.score = builder.score;
    }

    public void update(Scoreboard scoreboard) {
        if (!sidebar.getPlayer().isOnline()) return;

        if (scoreboard.getTeam("line" + getScore()) == null) {
            team = scoreboard.registerNewTeam("line" + getScore());
            team.addEntry(COLOR_CHAR + Integer.toHexString(getScore()));

            sidebar.getObjective().getScore(team.getEntries().toArray()[0].toString()).setScore(score);
        }

        String[] splittedText = this.splitText();

        team.setPrefix(splittedText[0]);
        team.setSuffix(splittedText[1]);
    }

    public String[] splitText() {
        String text = this.text.replacePlaceholders(sidebar.getPlayer());
        StringBuilder prefix = new StringBuilder(text.substring(0, Math.min(text.length(), 16)));
        StringBuilder suffix = new StringBuilder(text.length() > 16
                ? text.substring(16, Math.min(text.length(), 28))
                : "");

        if (prefix.length() == 16 && prefix.charAt(15) == COLOR_CHAR) {
            suffix.insert(0, COLOR_CHAR);
            prefix = new StringBuilder(text.substring(0, 15));
        }

        String prefixColor = getLastColors(prefix.toString());

        return new String[]{
                prefix.toString(),
                (prefixColor.matches("(" + COLOR_CHAR + "[k-o])+") || prefixColor.isEmpty() ? "" + RESET : "") +
                        (suffix.toString().matches("(" + COLOR_CHAR + "([0-9]|[a-f]))+.*") ? "" : prefixColor) +
                        suffix
        };
    }

    public UpdatableText getText() {
        return text;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Line line = (Line) o;
        return score == line.score && text.equals(line.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, score);
    }

    public static class Builder {
        private Sidebar sidebar;
        private UpdatableText text;
        private Integer score;

        public Builder sidebar(Sidebar sidebar) {
            this.sidebar = sidebar;
            return this;
        }

        public Builder text(UpdatableText text) {
            this.text = text;
            return this;
        }

        public Builder score(int score) {
            this.score = score;
            return this;
        }

        public Line build() {
            if (sidebar == null) {
                throw new IllegalStateException("Sidebar is not defined");
            }
            if (text == null) {
                text = bwPlayer -> "";
            }
            if (score == null) {
                if (sidebar.getLines() == null) {
                    throw new IllegalStateException("Score is not defined");
                }
                score = sidebar.getLines().size();
            }
            if (score > 15) {
                throw new IllegalStateException("Maximum score of 15 has been exceeded");
            }

            return new Line(this);
        }
    }
}