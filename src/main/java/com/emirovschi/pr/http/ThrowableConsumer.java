package com.emirovschi.pr.http;

@FunctionalInterface
public interface ThrowableConsumer<T, E extends Exception>
{
    void accept(T item) throws E;
}
