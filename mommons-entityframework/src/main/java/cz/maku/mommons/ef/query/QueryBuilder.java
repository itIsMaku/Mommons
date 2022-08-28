package cz.maku.mommons.ef.query;

public interface QueryBuilder {

    default ConditionBuilder where() {
        return new ConditionBuilder();
    }

    String build();

}
