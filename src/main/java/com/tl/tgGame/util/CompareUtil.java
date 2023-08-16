package com.tl.tgGame.util;

/**
 * @author T.J
 * @date 2022/1/20 21:36
 */
public class CompareUtil {
    @SuppressWarnings("unchecked")
    public static <T> boolean isGreaterThan(Comparable<T> a, Comparable<T> b) {
        return a.compareTo((T) b) > 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean isLessThan(Comparable<T> a, Comparable<T> b) {
        return a.compareTo((T) b) < 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean equals(Comparable<T> a, Comparable<T> b) {
        return a.compareTo((T) b) == 0;
    }
}
