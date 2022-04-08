package com.cg.lab4;

@FunctionalInterface
public interface ThriFunction<T, U, V, W, R> {

    R apply(T t, U u, V v, W w);
}
