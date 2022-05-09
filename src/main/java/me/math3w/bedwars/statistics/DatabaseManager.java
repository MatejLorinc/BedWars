package me.math3w.bedwars.statistics;

import me.math3w.bedwars.config.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class DatabaseManager {
    private static DatabaseManager instance;

    private final Database statisticsDatabase;

    public DatabaseManager(ConfigurationSection sqlConfig) {
        this.statisticsDatabase = new Database.Builder()
                .host(sqlConfig.getString("host"))
                .port(sqlConfig.getInt("port"))
                .database(sqlConfig.getString("database"))
                .username(sqlConfig.getString("username"))
                .password(sqlConfig.getString("password"))
                .table(sqlConfig.getString("table"))
                .build();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager(ConfigManager.getInstance().getSqlConfig());
        }
        return instance;
    }

    public Database getStatisticsDatabase() {
        return statisticsDatabase;
    }

    public void addStatistic(UUID uuid, Statistic statistic) {
        statisticsDatabase.set(uuid, statistic, statisticsDatabase.get(uuid, statistic) + 1);

        switch (statistic) {
            case WINS:
                addStatistic(uuid, Statistic.WIN_STREAK);
                break;
            case DRAWS:
            case LOSES:
                statisticsDatabase.set(uuid, Statistic.WIN_STREAK, 0);
                break;
            case WIN_STREAK:
                int winStreak = statisticsDatabase.get(uuid, statistic);

                if (winStreak > statisticsDatabase.get(uuid, Statistic.HIGHEST_STREAK)) {
                    statisticsDatabase.set(uuid, Statistic.HIGHEST_STREAK, winStreak);
                }
        }
    }
}
