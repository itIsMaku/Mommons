package cz.maku.mommons.utils;

import lombok.Data;

@Data
public class Pair<F, S> {

    private final F first;
    private final S second;

}
