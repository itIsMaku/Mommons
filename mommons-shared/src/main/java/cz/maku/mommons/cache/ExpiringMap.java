package cz.maku.mommons.cache;

import cz.maku.mommons.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpiringMap<K, V> {

    private final int expireAfter;
    private final ChronoUnit unit;
    private final Map<K, Pair<V, LocalDateTime>> map;

    public ExpiringMap(int expireAfter, ChronoUnit unit) {
        this.expireAfter = expireAfter;
        this.unit = unit;
        map = new ConcurrentHashMap<>();
    }

    public static <K, V> ExpiringMap<K, V> from(int expireAfter, ChronoUnit unit) {
        return new ExpiringMap<>(expireAfter, unit);
    }

    public void add(K key, V value) {
        renew(key, value);
    }

    public void renew(K key, V value) {
        map.put(key, new Pair<>(value, LocalDateTime.now()));
    }

    public void delete(K key) {
        map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public Map<K, Pair<V, LocalDateTime>> getData() {
        return map;
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map.values().stream().filter(pair -> pair.getFirst() == value).findFirst().orElse(null) != null;
    }

    @Nullable
    public V get(K key) {
        Pair<V, LocalDateTime> pair = map.get(key);
        if (pair == null) return null;
        LocalDateTime initTime = pair.getSecond();
        if (unit.between(initTime, LocalDateTime.now()) >= expireAfter) {
            map.put(key, new Pair<>(null, null));
        }
        Pair<V, LocalDateTime> pair2 = map.get(key);
        if (pair2 == null) return null;
        return pair2.getFirst();
    }
}
