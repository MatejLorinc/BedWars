package me.math3w.bedwars.statistics;

import com.google.common.base.Strings;
import me.math3w.bedwars.utils.Utils;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

public class Database {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String table;

    private Connection connection;

    private Database(Builder builder) {
        host = builder.host;
        port = builder.port;
        database = builder.database;
        username = builder.username;
        password = builder.password;
        table = builder.table;

        connect();
        if (!isConnected()) return;
        createTable();
    }

    private void connect() {
        if (isConnected()) return;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" +
                    host + ":" + port + "/" + database + "?useSSL=false", username, password);
        } catch (SQLException exception) {
            Bukkit.getLogger().severe("Cannot connect database");
        }
    }

    private void createTable() {
        if (!isConnected()) return;

        StringBuilder statistics = new StringBuilder();
        for (Statistic statistic : Statistic.values()) {
            statistics.append(String.format(", %s INT(10)", statistic.name().toLowerCase()));
        }

        String sql = "CREATE TABLE IF NOT EXISTS " + table +
                "(player BINARY(16), PRIMARY KEY (player)" + statistics + ")";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean createPlayer(UUID uuid) {
        if (!isConnected()) return false;
        if (exists(uuid)) return false;

        StringBuilder statistics = new StringBuilder();
        for (Statistic statistic : Statistic.values()) {
            statistics.append(", ").append(statistic.name().toLowerCase());
        }

        String sql = "INSERT IGNORE INTO " + table + " (player" + statistics + ") " +
                "VALUES (?" + Strings.repeat(", 0", Statistic.values().length) + ")";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBinaryStream(1, Utils.convertUniqueId(uuid));
            ps.executeUpdate();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean exists(UUID uuid) {
        if (!isConnected()) return false;

        String sql = "SELECT * " +
                "FROM " + table + " " +
                "WHERE player=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBinaryStream(1, Utils.convertUniqueId(uuid));

            try (ResultSet results = ps.executeQuery()) {
                return results.next();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public void set(UUID uuid, Statistic statistic, int value) {
        if (!isConnected()) return;

        createPlayer(uuid);

        String sql = "UPDATE " + table + " " +
                "SET " + statistic.name().toLowerCase() + "=? " +
                "WHERE player=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, value);
            ps.setBinaryStream(2, Utils.convertUniqueId(uuid));
            ps.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public int get(UUID uuid, Statistic statistic) {
        if (!isConnected())
            return 0;

        String sql = "SELECT " + statistic.name().toLowerCase() + " " +
                "FROM " + table + " " +
                "WHERE player=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBinaryStream(1, Utils.convertUniqueId(uuid));

            try (ResultSet results = ps.executeQuery()) {
                return results.next()
                        ? results.getInt(statistic.name().toLowerCase())
                        : 0;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return 0;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public static class Builder {
        private String host;
        private int port = -1;
        private String database;
        private String username;
        private String password = "";
        private String table;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder table(String table) {
            this.table = table;
            return this;
        }

        public Database build() {
            if (host == null) {
                throw new IllegalStateException("Host is not defined");
            }
            if (port == -1) {
                throw new IllegalStateException("Port is not defined");
            }
            if (database == null) {
                throw new IllegalStateException("Database is not defined");
            }
            if (username == null) {
                throw new IllegalStateException("Username is not defined");
            }
            if (table == null) {
                throw new IllegalStateException("Table is not defined");
            }

            return new Database(this);
        }
    }
}
