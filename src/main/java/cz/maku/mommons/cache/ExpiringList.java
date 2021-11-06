package cz.maku.mommons.cache;

import com.google.common.collect.Lists;
import cz.maku.mommons.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ExpiringList<E> {

    private final int expireAfter;
    private final ChronoUnit unit;
    private final List<Pair<E, LocalDateTime>> list;

    public ExpiringList(int expireAfter, ChronoUnit unit) {
        this.expireAfter = expireAfter;
        this.unit = unit;
        list = Lists.newArrayList();
    }

    public void add(E element) {
        renew(element);
    }

    public void renew(E element) {
        list.add(new Pair<>(element, LocalDateTime.now()));
    }

    public void delete(int index) {
        list.remove(index);
    }

    public void clear() {
        list.clear();
    }

    public List<E> getData() {
        List<E> l = Lists.newArrayList();
        for (Pair<E, LocalDateTime> pair : list) {
            l.add(pair.getFirst());
        }
        return l;
    }

    @Nullable
    public E get(int index) {
        Pair<E, LocalDateTime> pair = list.get(index);
        if (unit.between(pair.getSecond(), LocalDateTime.now()) >= expireAfter) {
            list.remove(index);
            return null;
        }
        return pair.getFirst();
    }
}
