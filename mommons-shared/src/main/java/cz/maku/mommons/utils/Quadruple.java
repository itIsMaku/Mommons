package cz.maku.mommons.utils;

import lombok.Data;

@Data
public class Quadruple<A, B, C, D> {

    private final A first;
    private final B second;
    private final C third;
    private final D fourth;

}
