package com.cg.lab4;

@FunctionalInterface
public interface ThriConsumer<T, U, V, W> {

    void accept(T t, U u, V v, W w);
}
