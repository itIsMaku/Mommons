package cz.maku.mommons.ef;

import com.google.common.collect.Maps;
import cz.maku.mommons.ef.repository.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public class RepositoryCache<ID, T> {

    private final Repository<ID, T> repository;
    private final ConcurrentMap<ID, T> cache;

    public RepositoryCache(Repository<ID, T> repository) {
        this.repository = repository;
        this.cache = Maps.newConcurrentMap();
    }

    public void cache(ID id, T object) {
        if (repository.cache(id, object)) {
            cache.put(id, object);
            return;
        }
        throw new RepositoryCachingException("Caching was not successful for id " + id + " in repository for object class " + object.getClass().getName() + "!");
    }

    public void remove(ID id) {
        T object = cache.get(id);
        if (repository.save(id, object)) {
            cache.remove(id);
            return;
        }
        throw new RepositoryCachingException("Removing from cache was not successful for id " + id + " in repository for object class " + object.getClass().getName() + "!");
    }

    public Optional<T> get(ID id) {
        return Optional.ofNullable(cache.get(id));
    }
}
