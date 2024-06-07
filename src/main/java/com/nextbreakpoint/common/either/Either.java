package com.nextbreakpoint.common.either;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Either implements a fluent interface for composing operations and handling errors.
 *
 * @param <V> the value's type
 */
public class Either<V> {
    private final V value;
    private final Exception exception;

    private Either(V value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }

    /**
     * Creates new instance with given value.
     *
     * @param value the value
     * @return new instance
     * @param <R> the value's type
     */
    public static <R> Either<R> success(R value) {
        return new Either<>(value, null);
    }

    /**
     * Creates new instance with given exception.
     *
     * @param exception the exception
     * @throws NullPointerException if supplier is null
     * @return new instance
     * @param <R> the value's type
     */
    public static <R> Either<R> failure(Exception exception) {
        Objects.requireNonNull(exception);
        return new Either<>(null, exception);
    }

    /**
     * Returns true when exception is not present.
     *
     * @return true when success
     */
    public boolean isSuccess() {
        return exception == null;
    }

    /**
     * Returns true when exception is present.
     *
     * @return true when failure
     */
    public boolean isFailure() {
        return !isSuccess();
    }

    /**
     * Returns the exception if present or null.
     *
     * @return the exception if present or null
     */
    public Exception exception() {
        return exception;
    }

    /**
     * Returns the value if present or null.
     *
     * @return the value if present or null
     */
    public V get() {
        return value;
    }

    /**
     * Returns the value if present or the default value.
     *
     * @param value the default value
     * @return the value if present or the default value
     */
    public V orElse(V value) {
        return orElseGet(() -> value);
    }

    /**
     * Returns the value if present or the supplied value.
     *
     * @param supplier the supplier
     * @throws NullPointerException if supplier is null
     * @return the value if present or the supplied value
     */
    public V orElseGet(Supplier<V> supplier) {
        Objects.requireNonNull(supplier);
        return this.value != null ? this.value : supplier.get();
    }

    /**
     * Throws the exception if the exception is present.
     *
     * @return the same either
     * @throws Exception the exception
     */
    public Either<V> orThrow() throws Exception {
        return orThrow(Function.identity());
    }

    /**
     * Throws the transformed exception if the exception is present.
     *
     * @param function the transformation
     * @return the same either
     * @throws E the transformed exception
     * @param <E> the exception's type
     */
    public <E extends Exception> Either<V> orThrow(Function<Exception, E> function) throws E {
        if (exception != null) {
            throw function.apply(exception);
        }

        return this;
    }

    /**
     * Returns same instance or alternative instance if failure.
     *
     * @param supplier the supplier of alternative instance
     * @return same instance or alternative instance if failure
     */
    public Either<V> or(Supplier<Either<V>> supplier) {
        if (exception != null) {
            return supplier.get();
        }

        return this;
    }

    /**
     * Creates new instance with given function.
     *
     * @param function the function
     * @return new instance
     * @throws NullPointerException if function is null
     * @param <R> the value's type
     */
    public <R> Either<R> map(Function<V, R> function) {
        Objects.requireNonNull(function);

        if (exception != null) {
            return Either.failure(exception);
        }

        try {
            return Either.success(function.apply(value));
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
    public <R> Either<R> flatMap(Function<V, Either<R>> function) {
        Objects.requireNonNull(function);

        if (exception != null) {
            return Either.failure(exception);
        }

        try {
            return Either.success(function.apply(value).orThrow().get());
        } catch (Exception e) {
            return Either.failure(e);
        }
    }

    /**
     * Returns a new observable.
     *
     * @return the observable
     */
    public Observable<V> observe() {
        return new Observable<>(this, null, null);
    }

    /**
     * Returns a new optional with same value.
     *
     * @return the optional
     */
    public Optional<V> optional() {
        return Optional.ofNullable(value);
    }

    /**
     * Returns a new stream with same value.
     *
     * @return the stream
     */
    public Stream<V> stream() {
        return Stream.ofNullable(value);
    }

    /**
     * Observable implements a fluent interface for observing the status of an Either.
     *
     * @param <V> the value's type
     */
    public static class Observable<V> {
        private final Either<V> either;
        private final Consumer<V> onSuccess;
        private final Consumer<Exception> onFailure;

        private Observable(Either<V> either, Consumer<V> onSuccess, Consumer<Exception> onFailure) {
            Objects.requireNonNull(either);
            this.either = either;
            this.onSuccess = onSuccess;
            this.onFailure = onFailure;
        }

        /**
         * Returns a new instance with given success callback.
         *
         * @param consumer the callback
         * @return new instance
         */
        public Observable<V> onSuccess(Consumer<V> consumer) {
            return new Observable<>(either, consumer, onFailure);
        }

        /**
         * Returns a new instance with given failure callback.
         *
         * @param consumer the callback
         * @return new instance
         */
        public Observable<V> onFailure(Consumer<Exception> consumer) {
            return new Observable<>(either, onSuccess, consumer);
        }

        /**
         * Returns the observed either.
         *
         * @return the observed either
         */
        public Either<V> get() {
            if (either.isSuccess()) {
                if (onSuccess != null) {
                    onSuccess.accept(either.get());
                }
            } else {
                if (onFailure != null) {
                    onFailure.accept(either.exception());
                }
            }

            return either;
        }
    }
}
