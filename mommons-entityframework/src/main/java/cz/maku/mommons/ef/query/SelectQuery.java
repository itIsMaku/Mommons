package cz.maku.mommons.ef.query;

import com.google.common.collect.Lists;

import java.util.List;

public class SelectQuery implements QueryBuilder {

    private final List<String> columns;
    private String table;
    private ConditionBuilder conditionBuilder;

    public SelectQuery() {
        this.columns = Lists.newArrayList();
    }

    public SelectQuery columns(String... columns) {
        this.columns.addAll(Lists.newArrayList(columns));
        return this;
    }

    public SelectQuery from(String table) {
        this.table = table;
        return this;
    }

    @Override
    public String build() {
        String columns = "*";
        if (!this.columns.isEmpty()) {
            columns = String.join(", ", this.columns);
        }
        return String.format("SELECT %s FROM %s%s;", columns, table, where().build());
    }

}
