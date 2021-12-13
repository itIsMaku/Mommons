package cz.maku.mommons.subscriber;

import com.google.common.collect.ImmutableMap;

public interface Subscriber<T, M> {

    void register(String key, T t);

    void unregister(String key);

    ImmutableMap<String, T> getSubscribers();

    void publish(String subscriber, M message);

}
