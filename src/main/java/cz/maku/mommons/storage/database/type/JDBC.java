package cz.maku.mommons.storage.database.type;

import cz.maku.mommons.storage.database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC implements Database<Connection> {

    private final String url;
    private final String username;
    private final String password;
    private Connection connection;

    public JDBC(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public JDBC(Connection connection) {
        this.url = null;
        this.username = null;
        this.password = null;
        this.connection = connection;
    }

    @Override
    public boolean connect() {
        if (!isConnected()) {
            try {
                if (url != null) {
                    connection = DriverManager.getConnection(url, username, password);
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
