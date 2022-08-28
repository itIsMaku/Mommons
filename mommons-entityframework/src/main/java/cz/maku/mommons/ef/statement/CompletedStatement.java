package cz.maku.mommons.ef.statement;

import cz.maku.mommons.ef.statement.record.Record;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public class CompletedStatement<T extends Statement> {

    private final T statement;
    private final List<? extends Record> records;

    public CompletedStatement(T statement, List<? extends Record> records) {
        this.statement = statement;
        this.records = records;
    }

    @Nullable
    public Record getRecord() {
        return records.get(0);
    }
}
