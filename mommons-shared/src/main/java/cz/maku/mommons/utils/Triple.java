package cz.maku.mommons.utils;

import lombok.Data;

@Data
public class Triple<A, B, C> {

    private final A first;
    private final B second;
    private final C third;

}