package cz.maku.mommons.ef.statement;

import cz.maku.mommons.ef.statement.record.Record;

import java.sql.Connection;
import java.util.List;

public interface Statement {

    CompletedStatement<?> complete(Connection connection);

    String getStatement();

    StatementType getType();

    void setArgumentValue(int id, Object value);

    int getArgumentsCount();

    List<? extends Record> query(Connection connection);

}
