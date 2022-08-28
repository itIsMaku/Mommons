package cz.maku.mommons.ef.statement.record;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class DefaultRecordImpl implements Record {

    private final Map<String, Object> columns = Maps.newHashMap();

    public void add(String column, Object value) {
        columns.put(column, value);
    }

    @Override
    public Object getObject(String column) {
        return columns.get(column);
    }

    @Override
    public String getString(String column) {
        return (String) columns.get(column);
    }

    @Override
    public int getInt(String column) {
        return (int) columns.get(column);
    }

    @Override
    public double getDouble(String column) {
        return (double) columns.get(column);
    }

    public <T> T getObjectFromJSON(Class<T> tClass, String column) {
        return new GsonBuilder().create().fromJson(getString(column), tClass);
    }
}
