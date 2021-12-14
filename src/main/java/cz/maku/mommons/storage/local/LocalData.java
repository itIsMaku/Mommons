package cz.maku.mommons.storage.local;

import cz.maku.mommons.Response;

public interface LocalData {

    Object getLocalValue(String key);

    Response setLocalValue(String key, Object value);

}
