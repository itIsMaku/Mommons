package cz.maku.mommons.ef.statement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cz.maku.mommons.ef.statement.record.DefaultRecordImpl;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class MySQLStatementImpl implements Statement {

    private final String statement;
    private final StatementType statementType;
    private final Map<Integer, Object> arguments;

    public MySQLStatementImpl(String statement, StatementType statementType, Map<Integer, Object> arguments) {
        this.statement = statement;
        this.statementType = statementType;
        this.arguments = arguments;
    }

    public MySQLStatementImpl(String statement, StatementType statementType) {
        this(statement, statementType, Maps.newHashMap());
    }

    @Override
    public CompletedStatement<MySQLStatementImpl> complete(Connection connection) {
        List<DefaultRecordImpl> query = Lists.newArrayList();
        System.out.println(statement);
        if (statementType.equals(StatementType.SELECT)) {
            query = query(connection);
        } else {
            query(connection);
        }
        return new CompletedStatement<>(this, query);
    }

    @Override
    public String getStatement() {
        return statement;
    }

    @Override
    public StatementType getType() {
        return statementType;
    }

    @Override
    public void setArgumentValue(int id, Object value) {
        arguments.put(id, value);
    }

    @Override
    public int getArgumentsCount() {
        return arguments.size();
    }

    @Override
    public List<DefaultRecordImpl> query(Connection connection) {
        try {
            PreparedStatement st = connection.prepareStatement(statement);
            for (Map.Entry<Integer, Object> entry : arguments.entrySet()) {
                st.setObject(entry.getKey(), entry.getValue());
            }
            ResultSet rs;
            if (statement.contains("SELECT")) {
                rs = st.executeQuery();
            } else if (statement.contains("CREATE TABLE IF NOT EXISTS ")) {
                st.executeUpdate();
                st.close();
                return Lists.newArrayList();
            } else {
                st.executeUpdate();
                //rs = st.executeQuery("SELECT * FROM {table};".replace("{table}", table));
                st.close();
                return Lists.newArrayList();
            }
            List<DefaultRecordImpl> result = Lists.newArrayList();
            while (rs.next()) {
                DefaultRecordImpl record = new DefaultRecordImpl();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String column = metaData.getColumnName(i);
                    record.add(column, rs.getObject(column));
                }
                result.add(record);
            }
            rs.close();
            st.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }
    }
}
