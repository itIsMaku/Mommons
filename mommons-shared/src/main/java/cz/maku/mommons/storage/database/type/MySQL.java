package cz.maku.mommons.storage.database.type;

import cz.maku.mommons.storage.database.SQLRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MySQL extends JDBC {

    private static MySQL instance;

    public MySQL(String ip, int port, String database, String username, String password, boolean useSSL, boolean autoReconnect) {
        super("jdbc:mysql://" + ip + ":" + port + "/" + database + "?allowPublicKeyRetrieval=true&autoReconnect=" + autoReconnect + "&useSSL=" + useSSL, username, password);
        instance = this;
    }

    public MySQL(Connection connection) {
        super(connection);
        instance = this;
    }

    public static MySQL getApi() {
        return instance;
    }

    public List<SQLRow> query(String table, String sql, Object... objects) {
        if (!isConnected()) {
            throw new RuntimeException("MySQL is not connected!");
        }
        try {
            if (!getConnection().isValid(60000)) {
                disconnect();
                connect();
            }
            sql = sql.replace("{table}", table);
            PreparedStatement st = getConnection().prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                st.setObject((i + 1), objects[i]);
            }
            ResultSet rs;
            if (sql.contains("SELECT")) {
                rs = st.executeQuery();
            } else if (sql.contains("CREATE TABLE IF NOT EXISTS ")) {
                st.executeUpdate();
                st.close();
                return new ArrayList<>();
            } else {
                st.executeUpdate();
                //rs = st.executeQuery("SELECT * FROM {table};".replace("{table}", table));
                st.close();
                return new ArrayList<>();
            }
            List<SQLRow> result = new ArrayList<>();
            while (rs.next()) {
                SQLRow sqlRow = new SQLRow();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String column = metaData.getColumnName(i);
                    sqlRow.add(column, rs.getObject(column));
                }
                result.add(sqlRow);
            }
            rs.close();
            st.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public CompletableFuture<List<SQLRow>> queryAsync(String table, String sql, Object... objects) {
        return CompletableFuture.supplyAsync(() -> query(table, sql, objects));
    }

    public Optional<SQLRow> single(String table, String sql, Object... objects) {
        List<SQLRow> rows = query(table, sql, objects);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rows.get(0));
    }

    public boolean existRow(String table, String column, Object clause) {
        List<SQLRow> rows = query(table, String.format("SELECT * FROM {table} WHERE %s = ?", column), clause);
        return !rows.isEmpty();
    }

    public CompletableFuture<Boolean> existRowAsync(String table, String column, Object clause) {
        return CompletableFuture.supplyAsync(() -> existRow(table, column, clause));
    }
}
