package cz.maku.mommons.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import cz.maku.mommons.Mommons;
import cz.maku.mommons.Response;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MySQLSavableData implements SavableData<String, Object> {

    private final MySQL mySQL;
    private final String tableName;
    private final String keyColumn;
    private final String valueColumn;
    private final String keyValue;

    public MySQLSavableData(MySQL mySQL, String tableName, String keyColumn, String valueColumn, String keyValue) {
        this.mySQL = mySQL;
        this.tableName = tableName;
        this.keyColumn = keyColumn;
        this.valueColumn = valueColumn;
        this.keyValue = keyValue;
    }

    public MySQLSavableData(String tableName, String keyColumn, String valueColumn, String keyValue) {
        this(MySQL.getApi(), tableName, keyColumn, valueColumn, keyValue);
    }

    private Map<String, Object> getValuesMutable() {
        Optional<SQLRow> row = mySQL.single(tableName, String.format("SELECT data FROM {table} WHERE %s = ?", keyColumn), keyValue);
        if (!row.isPresent()) return Collections.emptyMap();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return row.get().getJsonObject(valueColumn, type);
    }

    public Map<String, Object> getValues() {
        return ImmutableMap.copyOf(getValuesMutable());
    }

    @Override
    public Object getValue(String key) {
        return getValues().get(key);
    }

    public CompletableFuture<Object> getValueAsync(String key) {
        return CompletableFuture.supplyAsync(() -> getValue(key), Mommons.ES);
    }

    @Override
    public Response setValue(String key, Object value, boolean replace) {
        Map<String, Object> values = getValuesMutable();
        if (values.containsKey(key) && !replace) {
            return new Response(Response.Code.ERROR, "Key already exists and replace is false");
        }
        if (value == null) {
            values.remove(key);
        } else {
            values.put(key, value);
        }
        return Response.from(() -> mySQL.query(tableName, String.format("UPDATE {table} SET %s = ? WHERE %s = ?", valueColumn, keyColumn), Mommons.GSON.toJson(values), keyValue));
    }

    @Override
    public Response setValue(String key, Object value) {
        return setValue(key, value, true);
    }

    public CompletableFuture<Response> setValueAsync(String key, Object value, boolean replace) {
        return CompletableFuture.supplyAsync(() -> setValue(key, value, replace), Mommons.ES);
    }

    public Response setMultipleValues(Map<String, Object> values) {
        return Response.from(() -> {
            Map<String, Object> currentValues = getValuesMutable();
            currentValues.putAll(values);
            mySQL.query(tableName, String.format("UPDATE {table} SET %s = ? WHERE %s = ?", valueColumn, keyColumn), Mommons.GSON.toJson(currentValues), keyValue);
        });
    }

    public CompletableFuture<Response> setMultipleValuesAsync(Map<String, Object> values) {
        return CompletableFuture.supplyAsync(() -> setMultipleValues(values), Mommons.ES);
    }
}
