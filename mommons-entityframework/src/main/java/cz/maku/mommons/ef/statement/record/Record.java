package cz.maku.mommons.ef.statement.record;

import cz.maku.mommons.Mommons;

public interface Record {

    Object getObject(String column);

    String getString(String column);

    int getInt(String column);

    double getDouble(String column);

    default <T> T getObjectFromJSON(Class<T> tClass, String column) {
        return Mommons.GSON.fromJson(getString(column), tClass);
    }
}
