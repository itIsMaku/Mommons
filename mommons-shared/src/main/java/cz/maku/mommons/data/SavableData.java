package cz.maku.mommons.data;

import cz.maku.mommons.Response;

public interface SavableData<K, V> {

    V getValue(K key);

    Response setValue(K key, V value, boolean replace);

    Response setValue(K key, V value);

}
