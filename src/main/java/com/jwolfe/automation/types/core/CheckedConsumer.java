package com.jwolfe.automation.types.core;

@FunctionalInterface
public interface CheckedConsumer<T> {
    void accept(T t) throws InterruptedException;
}
