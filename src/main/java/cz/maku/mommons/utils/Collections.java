package cz.maku.mommons.utils;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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


}
