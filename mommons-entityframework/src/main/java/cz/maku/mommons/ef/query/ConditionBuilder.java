package cz.maku.mommons.ef.query;

import com.google.common.collect.Lists;

import java.util.List;

public class ConditionBuilder implements QueryBuilder {

    private final List<String> conditions;

    public ConditionBuilder() {
        this.conditions = Lists.newArrayList();
    }

    public ConditionBuilder equals(String key, Object value) {
        conditions.add(String.format("%s = '%s'", key, value));
        return this;
    }

    public ConditionBuilder contains(String key, Object value) {
        conditions.add(key + " LIKE '%" + value + "%'");
        return this;
    }

    public ConditionBuilder and() {
        conditions.add(" AND ");
        return this;
    }

    public ConditionBuilder or() {
        conditions.add(" OR ");
        return this;
    }

    @Override
    public String build() {
        if (conditions.isEmpty()) return "WHERE TRUE";
        StringBuilder queryCondition = new StringBuilder(" WHERE ");
        for (String condition : conditions) {
            queryCondition.append(condition);
        }
        return queryCondition.toString();
    }
}
