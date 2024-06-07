package com.nextbreakpoint.common.command;

import com.nextbreakpoint.common.either.Either;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Command implements a fluent interface for composing operations and execute them as single command.
 *
 * @param <V> the value's type
 * @author Andrea Medeghini
 */
public class Command<V> {
    private final Callable<V> callable;

    private Command(Callable<V> callable) {
        Objects.requireNonNull(callable);
        this.callable = callable;
    }

    /**
     * Creates new instance with given exception.
     *
     * @param exception the exception
     * @return new instance
     * @param <R> the value's type
     */
    public static <R> Command<R> error(Exception exception) {
        Objects.requireNonNull(exception);
        return of(() -> {
            throw exception;
        });
    }

    /**
     * Creates new instance with given value.
     *
     * @param value the value
     * @return new instance
     * @param <R> the value's type
     */
    public static <R> Command<R> value(R value) {
        return of(() -> value);
    }

    /**
     * Creates new instance from callable.
     *
     * @param callable the callable
     * @return new instance
     * @throws NullPointerException if callable is null
     * @param <R> the value's type
     */
    public static <R> Command<R> of(Callable<R> callable) {
        return new Command<>(callable);
    }

    /**
     * Creates new instance from either.
     *
     * @param either the either
     * @return new instance
     * @throws NullPointerException if either is null
     * @param <R> the value's type
     */
    public static <R> Command<R> of(Either<R> either) {
        Objects.requireNonNull(either);
        return new Command<>(() -> either.orThrow().get());
    }

    /**
     * Execute and returns result.
     *
     * @return the result
     */
    public Either<V> execute() {
        try {
            return Either.success(callable.call());
        } catch (Exception e) {
            return Either.failure(e);
        }
    }

    /**
     * Creates new instance with given function.
     *
     * @param function the function
     * @return new instance
     * @throws NullPointerException if function is null
     * @param <R> the value's type
     */
    public <R> Command<R> map(Function<V, R> function) {
        Objects.requireNonNull(function);
        return new Command<>(() -> function.apply(evaluate()));
    }

    /**
     * Creates new instance with given function.
     *
     * @param function the function
     * @return new instance
     * @throws NullPointerException if function is null
     * @param <R> the value's type
     */
    public <R> Command<R> flatMap(Function<V, Command<R>> function) {
        Objects.requireNonNull(function);
        return new Command<>(() -> function.apply(evaluate()).evaluate());
    }

    private V evaluate() throws Exception {
        return execute().orThrow().orElse(null);
    }
}
