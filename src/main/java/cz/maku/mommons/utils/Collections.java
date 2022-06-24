package cz.maku.mommons.utils;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Collections {

    @NotNull
    public static <E> E random(Collection<E> collection) {
        int num = (int) (Math.random() * collection.size());
        for (E e : collection) {
            if (--num < 0) {
                return e;
            }
        }
        throw new IllegalArgumentException("Can't find random value.");
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
