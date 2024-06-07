package com.nextbreakpoint.common.function;

import java.util.Objects;

/**
 * This class represents a function which can throw exceptions.
 *
 * @param <T> the input's type
 * @param <R> the output's type
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {
    /**
     * Apply a function to the given input.
     *
     * @param value the input
     * @return the output
     * @throws Exception some error
     */
    R apply(T value) throws Exception;

    /**
     * Returns a composed function that first applies the before function to its input, and then applies this function to the result.
     *
     * @param before the before function
     * @return the composed function
     * @param <V> the output's type
     */
    default <V> ThrowingFunction<V, R> compose(ThrowingFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to its input, and then applies the after function to the result.
     *
     * @param after the after function
     * @return the composed function
     * @param <V> the output's type
     */
    default <V> ThrowingFunction<T, V> andThen(ThrowingFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns an function that always returns its input argument.
     *
     * @return the input
     * @param <T> the input's type
     */
    static <T> ThrowingFunction<T, T> identity() {
        return t -> t;
    }
}
