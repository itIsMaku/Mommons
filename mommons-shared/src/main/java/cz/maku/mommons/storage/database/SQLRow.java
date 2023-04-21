package cz.maku.mommons.storage.database;

import cz.maku.mommons.Mommons;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SQLRow {

    private final Map<String, Object> columns;

    public SQLRow() {
        this.columns = new HashMap<>();
    }

    public void add(String column, Object value) {
        columns.put(column, value);
    }

    public Object getObject(String column) {
        return columns.get(column);
    }

    public String getString(String column) {
        return (String) columns.get(column);
    }

    public int getInt(String column) {
        return (int) columns.get(column);
    }

    public double getDouble(String column) {
        return (double) columns.get(column);
    }

    @Deprecated
    public <T> T getObjectFromJSON(Class<T> tClass, String column) {
        return Mommons.GSON.fromJson(getString(column), tClass);
    }

    public <T> T getJsonObject(String column, Type type) {
        return Mommons.GSON.fromJson(getString(column), type);
    }

    public <T> T getJsonObject(String column, Class<T> tClass) {
        return Mommons.GSON.fromJson(getString(column), tClass);
    }
}
