package cz.maku.mommons.storage.database;

import com.google.gson.GsonBuilder;

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

    public <T> T getObjectFromJSON(Class<T> tClass, String column) {
        return new GsonBuilder().create().fromJson(getString(column), tClass);
    }
}
