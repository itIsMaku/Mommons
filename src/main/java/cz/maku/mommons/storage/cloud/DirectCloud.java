package cz.maku.mommons.storage.cloud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Response;
import cz.maku.mommons.storage.database.SQLRow;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.annotation.Service;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Service
public class DirectCloud {

    @Getter
    private final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .create();

    @NotNull
    public Response insert(DirectCloudStorage directCloudStorage, String keyColumn, Object key, String valueColumn, Object value) {
        if (directCloudStorage.equals(DirectCloudStorage.SERVER) || directCloudStorage.equals(DirectCloudStorage.PLAYER)) {
            try {
                MySQL.getApi().query(directCloudStorage.getSqlTable(), String.format("INSERT INTO {table} (%s, %s) VALUES (?, ?);", keyColumn, valueColumn), key, value);
                return new Response(Response.Code.SUCCESS, null);
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionResponse(Response.Code.ERROR, "Exception during inserting key and value.", e);
            }
        }
        return new Response(Response.Code.ERROR, "DirectCloudStorage was not found.");
    }

    @NotNull
    public Response update(DirectCloudStorage directCloudStorage, String keyColumn, Object key, String valueColumn, Object value) {
        if (directCloudStorage.equals(DirectCloudStorage.SERVER) || directCloudStorage.equals(DirectCloudStorage.PLAYER)) {
            try {
                MySQL.getApi().query(directCloudStorage.getSqlTable(), String.format("UPDATE {table} SET %s = ? WHERE %s = ?;", valueColumn, keyColumn), value, key);
                return new Response(Response.Code.SUCCESS, null);
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionResponse(Response.Code.ERROR, "Exception during updating cloud value.", e);
            }
        }
        return new Response(Response.Code.ERROR, "DirectCloudStorage was not found.");
    }

    @Nullable
    public Object get(DirectCloudStorage directCloudStorage, String keyColumn, Object key, String valueColumn) {
        if (directCloudStorage.equals(DirectCloudStorage.SERVER) || directCloudStorage.equals(DirectCloudStorage.PLAYER)) {
            try {
                List<SQLRow> rows = MySQL.getApi().query(directCloudStorage.getSqlTable(), String.format("SELECT * FROM {table} WHERE %s = ?;", keyColumn), key);
                if (rows.isEmpty()) return null;
                if (rows.get(0) == null) return null;
                return rows.get(0).getObject(valueColumn);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
