package cz.maku.mommons.storage.local;

import cz.maku.mommons.ExceptionResponse;
import cz.maku.mommons.Response;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface LocalData {

    Object getLocalValue(String key);

    Response setLocalValue(String key, Object value);

    @NotNull
    default Response setLocalValueWithResponse(String key, Object value, Map<String, Object> localData) {
        try {
            if (localData.containsKey(key) && value == null) {
                localData.remove(key);
                return new Response(Response.Code.SUCCESS, null);
            }
            localData.put(key, value);
            return new Response(Response.Code.SUCCESS, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ExceptionResponse(Response.Code.ERROR, "There is an exception during setting local value.", e);
        }
    }
}
