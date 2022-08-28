package cz.maku.mommons.subscriber.local;

public interface LocalSubscriber {

    void onReceive(String subscriber, Object message);

}
