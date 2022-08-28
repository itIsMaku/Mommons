package cz.maku.mommons.ef.test;

import com.google.common.reflect.TypeToken;
import cz.maku.mommons.Mommons;
import cz.maku.mommons.ef.converter.TypeConverter;

import java.lang.reflect.Type;
import java.util.Map;

public class CurrenciesConverter implements TypeConverter<Map<String, Integer>, String> {
    @Override
    public String convertToColumn(Map<String, Integer> map) {
        return Mommons.GSON.toJson(map);
    }

    @Override
    public Map<String, Integer> convertToEntityField(String s) {
        Type type = new TypeToken<Map<String, Integer>>(){}.getType();
        return Mommons.GSON.fromJson(s, type);
    }
}
