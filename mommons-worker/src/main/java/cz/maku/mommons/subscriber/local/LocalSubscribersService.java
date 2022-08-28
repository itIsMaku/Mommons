package cz.maku.mommons.subscriber.local;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import cz.maku.mommons.subscriber.Subscriber;
import cz.maku.mommons.worker.annotation.Initialize;
import cz.maku.mommons.worker.annotation.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LocalSubscribersService implements Subscriber<LocalSubscriber, Object> {

    private Map<String, LocalSubscriber> subscribers;

    @Initialize
    public void initialize() {
        subscribers = Maps.newConcurrentMap();
    }

    public void register(String key, LocalSubscriber subscriber) {
        subscribers.put(key, subscriber);
    }

    public void unregister(String key) {
        subscribers.remove(key);
    }

    public ImmutableMap<String, LocalSubscriber> getSubscribers() {
        return ImmutableMap.copyOf(subscribers);
    }

    public void publish(String subscriber, Object object) {
        for (Map.Entry<String, LocalSubscriber> entry : subscribers.entrySet().stream().filter(entry -> entry.getKey().equalsIgnoreCase(subscriber)).collect(Collectors.toList())) {
            entry.getValue().onReceive(subscriber, object);
        }
    }
}
