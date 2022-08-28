package cz.maku.mommons.cache;

@FunctionalInterface
public interface QueueTask<V> {

    V complete();

}
