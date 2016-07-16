/*
 * This file is part of Try
 * 
 * Copyright (c) 2016, Andrea Medeghini
 * All rights reserved.
 */
package com.nextbreakpoint;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Try implements a monad for handling checked and unchecked exceptions.
 * 
 * @author Andrea Medeghini
 *
 * @param <V> the type of returned value
 * @param <E> the type of captured exception
 */
public abstract class Try<V, E extends Exception> {
    /**
     * The function to transform an exception into expected exception.
     */
    protected final Function<Exception, E> mapper;

	/**
	 * The filter to apply when getting or mapping value.
	 */
	protected final Predicate<Object> filter;

	/**
	 * The handler to call when result is success.
	 */
	protected final Consumer<Optional<Object>> onSuccess;

	/**
	 * The handler to call when result is failure.
	 */
	protected final Consumer<Exception> onFailure;

	/**
	 * Returns true if exception occurred.
	 * @return true when exception if present 
	 */
	public abstract boolean isFailure();

	/**
	 * Returns true if no exception occurred.
	 * @return true when exception if not present
	 */
	public abstract boolean isSuccess();

	/**
	 * Returns true if value is present.
	 * @return true when value is present
	 */
	public abstract boolean isPresent();

	/**
	 * Invokes consumer if exception occurred.
	 * @param consumer the consumer
	 */
	public abstract void ifFailure(Consumer<E> consumer);

	/**
	 * Invokes consumer if no exception occurred.
	 * @param consumer the consumer
	 */
	public abstract void ifSuccess(Consumer<Optional<V>> consumer);

	/**
	 * Invokes consumer if value is present.
	 * @param consumer the consumer
	 */
	public abstract void ifPresent(Consumer<V> consumer);

	/**
	 * Invokes consumer if value is present or throws exception if failure.
	 * @param consumer the consumer
	 * @throws E the exception
	 */
	public abstract void ifPresentOrThrow(Consumer<V> consumer) throws E;

	/**
	 * Returns the value.
	 * @return the value
	 * @throws NoSuchElementException if value not present.
	 */
	public abstract V get();

	/**
	 * Returns the value if present or returns the default value.
	 * @param value the default value
	 * @return the value
	 */
	public abstract V orElse(V value);

	/**
	 * Returns the value if present or invokes the supplier to get the value.
	 * @param supplier the supplier
	 * @return the value
	 */
	public abstract V orElseGet(Supplier<V> supplier);

	/**
	 * Returns the value if present or throws exception if failure.
	 * @return the value
	 * @throws E the exception
	 * @throws NoSuchElementException if value not present.
	 */
	public abstract V orThrow() throws E;

	/**
	 * Returns the value if present or throws exception if failure or returns the default value.
	 * @param value the default value
	 * @return the value
	 * @throws E the exception
	 */
	public abstract V orThrow(V value) throws E;

	/**
	 * Returns optional of value.
	 * @return new optional
	 */
	public abstract Optional<V> value();

	/**
	 * Throws exception if failure.
	 * @throws E the exception
	 */
	public abstract void throwIfFailure() throws E;

	/**
	 * Creates new instance mapper given mapping function.
	 * @param func the function
	 * @param <R> the result's value type
	 * @return new instance
	 * @throws NullPointerException if func is null.
	 */
	public abstract <R> Try<R, E> map(Function<V, R> func);

	/**
	 * Creates new instance mapper given mapping function.
	 * @param func the function
	 * @param <R> the result's value type
	 * @return new instance
	 * @throws NullPointerException if func is null.
	 */
	public abstract <R> Try<R, E> flatMap(Function<V, Try<R, E>> func);

	/**
	 * Creates new instance which executes alternative callable if failure.
	 * @param callable the callable
	 * @return new instance
	 * @throws NullPointerException if callable is null.
	 */
	public abstract Try<V, E> or(Callable<V> callable);

	/**
	 * Creates new instance which executes consecutive callable if success.
	 * @param callable the callable
	 * @return new instance
	 * @throws NullPointerException if callable is null.
	 */
	public abstract Try<V, E> and(Callable<V> callable);

	/**
	 * Creates new instance with given consumer of success event.
	 * @param consumer the consumer
	 * @return new instance
	 */
	public abstract Try<V, E> onSuccess(Consumer<Optional<Object>> consumer);

	/**
	 * Creates new instance with given consumer of failure event.
	 * @param consumer the consumer
	 * @return new instance
	 */
	public abstract Try<V, E> onFailure(Consumer<Exception> consumer);

    /**
     * Creates new instance with given exception mapper.
     * @param mapper the mapper
	 * @param <X> the exception type
     * @return new instance
	 * @throws NullPointerException if mapper is null.
     */
	public abstract <X extends Exception> Try<V, X> mapper(Function<Exception, X> mapper);

	/**
	 * Creates new instance with given value filter.
	 * @param filter the filter
	 * @return new instance
	 * @throws NullPointerException if filter is null.
	 */
	public abstract Try<V, E> filter(Predicate<Object> filter);

    /**
     * Creates new instance with given callable.
     * @param callable the callable
	 * @param <R> the result's value type
     * @return new instance
     */
    public static <R> Try<R, Exception> of(Callable<R> callable) {
		return new TryCallable<>(defaultMapper(), defaultFilter(), callable);
    }

	/**
     * Creates new instance with given exception.
     * @param exception the exception
	 * @param <R> the result's value type
     * @return new instance
     */
    public static <R> Try<R, Exception> failure(Exception exception) {
		return new TryFailure<>(defaultMapper(), defaultFilter(), exception);
	}

    /**
     * Creates new instance with given value.
     * @param value the value
	 * @param <R> the result's value type
     * @return new instance
     */
    public static <R> Try<R, Exception> success(R value) {
		return new TrySuccess<>(defaultMapper(), defaultFilter(), value);
	}

	private Try(Function<Exception, E> mapper, Predicate<Object> filter, Consumer<Optional<Object>> onSuccess, Consumer<Exception> onFailure) {
		Objects.requireNonNull(mapper);
		Objects.requireNonNull(filter);
		this.mapper = mapper;
		this.filter = filter;
		this.onSuccess = onSuccess;
		this.onFailure = onFailure;
	}

	private static Function<Exception, Exception> defaultMapper() {
		return x -> x;
	}

	private static Predicate<Object> defaultFilter() {
		return v -> v != null;
	}

	private static class TryFailure<V, E extends Exception> extends Try<V, E> {
		private final E exception;

		public TryFailure(Function<Exception, E> mapper, Predicate<Object> filter, Consumer<Optional<Object>> onSuccessHandler, Consumer<Exception> onFailureHandler, E exception) {
			super(mapper, filter, onSuccessHandler, onFailureHandler);
			Objects.requireNonNull(exception);
			this.exception = exception;
		}

		public TryFailure(Function<Exception, E> mapper, Predicate<Object> filter, E exception) {
			this(mapper, filter, null, null, exception);
		}

		public boolean isFailure() {
			notifyEvent();
			return true;
		}

		public boolean isSuccess() {
			notifyEvent();
			return false;
		}

		public boolean isPresent() {
			notifyEvent();
			return false;
		}

		public void ifFailure(Consumer<E> consumer) {
			notifyEvent();
			consumer.accept(exception);
		}

		public void ifSuccess(Consumer<Optional<V>> consumer) {
			notifyEvent();
		}

		public void ifPresent(Consumer<V> consumer) {
			notifyEvent();
		}

		public void ifPresentOrThrow(Consumer<V> consumer) throws E {
			notifyEvent();
			throw exception;
		}

		public V get() {
			notifyEvent();
			throw new NoSuchElementException("Failure doesn't have any value");
		}

		public V orElse(V value) {
			notifyEvent();
			return value;
		}

		public V orElseGet(Supplier<V> supplier) {
			notifyEvent();
			return supplier.get();
		}

		public V orThrow() throws E {
			notifyEvent();
			throw exception;
	    }

		public V orThrow(V value) throws E {
			notifyEvent();
			throw exception;
	    }
		
		public Optional<V> value() {
			notifyEvent();
			return Optional.empty();
		}

		public void throwIfFailure() throws E {
			notifyEvent();
			throw exception;
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			Objects.requireNonNull(func);
			return new TryFailure<>(mapper, defaultFilter(), onSuccess, onFailure, exception);
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			Objects.requireNonNull(func);
			return new TryFailure<>(mapper, defaultFilter(), onSuccess, onFailure, exception);
		}

		public Try<V, E> or(Callable<V> callable) {
			Objects.requireNonNull(callable);
			return create(() -> orTry(onFailure, callable));
		}

		public Try<V, E> and(Callable<V> callable) {
			Objects.requireNonNull(callable);
			return new TryFailure<>(mapper, defaultFilter(), onSuccess, onFailure, exception);
		}

		public Try<V, E> onSuccess(Consumer<Optional<Object>> consumer) {
			return new TryFailure<>(mapper, filter, consumer, onFailure, exception);
		}

		public Try<V, E> onFailure(Consumer<Exception> consumer) {
			return new TryFailure<>(mapper, filter, onSuccess, consumer, exception);
		}

		public <X extends Exception> Try<V, X> mapper(Function<Exception, X> mapper) {
			return new TryFailure<>(mapper, filter, onSuccess, onFailure, mapper.apply(exception));
		}

		public Try<V, E> filter(Predicate<Object> filter) {
			return new TryFailure<>(mapper, filter, onSuccess, onFailure, exception);
		}

		private void notifyEvent() {
			Optional.ofNullable(onFailure).ifPresent(consumer -> consumer.accept(exception));
		}

		private <R> Try<R, E> create(Callable<R> callable) {
			return new TryCallable<>(mapper, defaultFilter(), onSuccess, onFailure, callable);
		}

		private V call(Callable<V> callable) throws Exception {
			return callable.call();
		}

		private V orTry(Consumer<Exception> onFailure, Callable<V> callable) throws Exception {
			Optional.ofNullable(onFailure).ifPresent(consumer -> consumer.accept(exception));
			return call(callable);
		}
	}

	private static class TrySuccess<V, E extends Exception> extends Try<V, E> {
		private final V value;

		public TrySuccess(Function<Exception, E> mapper, Predicate<Object> filter, Consumer<Optional<Object>> onSuccessHandler, Consumer<Exception> onFailureHandler, V value) {
			super(mapper, filter, onSuccessHandler, onFailureHandler);
			this.value = value;
		}

		public TrySuccess(Function<Exception, E> mapper, Predicate<Object> filter, V value) {
			this(mapper, filter, null, null, value);
		}

		public boolean isFailure() {
			notifyEvent();
			return false;
		}

		public boolean isSuccess() {
			notifyEvent();
			return true;
		}

		public boolean isPresent() {
			return value().isPresent();
		}

		public void ifFailure(Consumer<E> consumer) {
			notifyEvent();
		}

		public void ifSuccess(Consumer<Optional<V>> consumer) {
			consumer.accept(value());
		}

		public void ifPresent(Consumer<V> consumer) {
			value().ifPresent(consumer);
		}

		public void ifPresentOrThrow(Consumer<V> consumer) {
			value().ifPresent(consumer);
		}

		public V get() {
			return value().get();
		}

		public V orElse(V value) {
			return value().orElse(value);
		}

		public V orElseGet(Supplier<V> supplier) {
			return value().orElseGet(supplier);
		}

		public V orThrow() throws E {
	        return value().get();
	    }

		public V orThrow(V value) throws E {
	        return value().orElse(value);
	    }
		
		public Optional<V> value() {
			notifyEvent();
			return evaluate();
		}

		public void throwIfFailure() throws E {
			notifyEvent();
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			Objects.requireNonNull(func);
			return create(() -> evaluate().map(func).orElse(null));
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			Objects.requireNonNull(func);
			return create(() -> evaluate().map(func).orElseGet(() -> empty()).orThrow(null));
		}

		public Try<V, E> or(Callable<V> callable) {
			Objects.requireNonNull(callable);
			return new TrySuccess<>(mapper, defaultFilter(), onSuccess, onFailure, value);
		}

		public Try<V, E> and(Callable<V> callable) {
			Objects.requireNonNull(callable);
			return create(() -> andTry(onSuccess, callable));
		}

		public Try<V, E> onSuccess(Consumer<Optional<Object>> consumer) {
			return new TrySuccess<>(mapper, filter, consumer, onFailure, value);
		}

		public Try<V, E> onFailure(Consumer<Exception> consumer) {
			return new TrySuccess<>(mapper, filter, onSuccess, consumer, value);
		}

		public <X extends Exception> Try<V, X> mapper(Function<Exception, X> mapper) {
			return new TrySuccess<>(mapper, filter, onSuccess, onFailure, value);
		}

		public Try<V, E> filter(Predicate<Object> filter) {
			return new TrySuccess<>(mapper, filter, onSuccess, onFailure, value);
		}

		private Optional<V> evaluate() {
			return Optional.ofNullable(value).filter(filter);
		}

		private <R> Try<R, E> empty() {
			return new TrySuccess<>(mapper, defaultFilter(), onSuccess, onFailure, null);
		}

		private <R> Try<R, E> create(Callable<R> callable) {
			return new TryCallable<>(mapper, defaultFilter(), onSuccess, onFailure, callable);
		}

		private void notifyEvent() {
			Optional.ofNullable(onSuccess).ifPresent(consumer -> consumer.accept(Optional.ofNullable(value)));
		}

		private V call(Callable<V> callable) throws Exception {
			return callable.call();
		}

		private V andTry(Consumer<Optional<Object>> onSuccess, Callable<V> callable) throws Exception {
			Optional.ofNullable(onSuccess).ifPresent(consumer -> consumer.accept(Optional.ofNullable(value)));
			return call(callable);
		}
	}

	private static class TryCallable<V, E extends Exception> extends Try<V, E> {
		private final Callable<V> callable;

		public TryCallable(Function<Exception, E> mapper, Predicate<Object> filter, Consumer<Optional<Object>> onSuccessHandler, Consumer<Exception> onFailureHandler, Callable<V> callable) {
			super(mapper, filter, onSuccessHandler, onFailureHandler);
			Objects.requireNonNull(callable);
			this.callable = callable;
		}

		public TryCallable(Function<Exception, E> mapper, Predicate<Object> filter, Callable<V> callable) {
			this(mapper, filter, null, null, callable);
		}

		public boolean isFailure() {
			return execute().isFailure();
		}

		public boolean isSuccess() {
			return execute().isSuccess();
		}

		public boolean isPresent() {
			return execute().isPresent();
		}

		public void ifFailure(Consumer<E> consumer) {
			execute().ifFailure(consumer);
		}

		public void ifSuccess(Consumer<Optional<V>> consumer) {
			execute().ifSuccess(consumer);
		}

		public void ifPresent(Consumer<V> consumer) {
			execute().ifPresent(consumer);
		}

		public void ifPresentOrThrow(Consumer<V> consumer) throws E {
			execute().ifPresentOrThrow(consumer);
		}

		public V get() {
			return execute().get();
		}

		public V orElse(V value) {
			return execute().orElse(value);
		}

		public V orElseGet(Supplier<V> supplier) {
			return execute().orElseGet(supplier);
		}

		public V orThrow() throws E {
			return execute().orThrow();
		}

		public V orThrow(V value) throws E {
			return execute().orThrow(value);
		}

		public Optional<V> value() {
			return execute().value();
		}

		public void throwIfFailure() throws E {
			execute().throwIfFailure();
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			Objects.requireNonNull(func);
			return create(() -> evaluate().map(func).orElse(null));
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			Objects.requireNonNull(func);
			return create(() -> evaluate().map(func).orElseGet(() -> empty()).orThrow(null));
		}

		public Try<V, E> or(Callable<V> callable) {
			Objects.requireNonNull(callable);
			return create(() -> orTry(onFailure, callable));
		}

		public Try<V, E> and(Callable<V> callable) {
			Objects.requireNonNull(callable);
			return create(() -> andTry(onSuccess, callable));
		}

		public Try<V, E> onSuccess(Consumer<Optional<Object>> consumer) {
			return new TryCallable<>(mapper, filter, consumer, onFailure, callable);
		}

		public Try<V, E> onFailure(Consumer<Exception> consumer) {
			return new TryCallable<>(mapper, filter, onSuccess, consumer, callable);
		}

		public <X extends Exception> Try<V, X> mapper(Function<Exception, X> mapper) {
			return new TryCallable<>(mapper, filter, onSuccess, onFailure, callable);
		}

		public Try<V, E> filter(Predicate<Object> filter) {
			return new TryCallable<>(mapper, filter, onSuccess, onFailure, callable);
		}

		private Try<V, E> execute() {
			try {
				return new TrySuccess<>(mapper, defaultFilter(), onSuccess, onFailure, evaluate().orElse(null));
			} catch (Exception e) {
				return new TryFailure<>(mapper, defaultFilter(), onSuccess, onFailure, mapper.apply(e));
			}
		}

		private Optional<V> evaluate() throws Exception {
			return evaluate(callable);
		}

		private Optional<V> evaluate(Callable<V> callable) throws Exception {
			return Optional.ofNullable(call(callable)).filter(filter);
		}

		private <R> Try<R, E> empty() {
			return new TrySuccess<>(mapper, defaultFilter(), onSuccess, onFailure, null);
		}

		private <R> Try<R, E> fail(E exception) {
			return new TryFailure<>(mapper, defaultFilter(), onSuccess, onFailure, exception);
		}

		private <R> Try<R, E> create(R value) {
			return new TrySuccess<>(mapper, defaultFilter(), onSuccess, onFailure, value);
		}

		private <R> Try<R, E> create(Callable<R> callable) {
			return new TryCallable<>(mapper, defaultFilter(), onSuccess, onFailure, callable);
		}

		private V call() throws Exception {
			return call(callable);
		}

		private V call(Callable<V> callable) throws Exception {
			return callable.call();
		}

		private V orTry(Consumer<Exception> onFailure, Callable<V> callable) throws Exception {
			try {
				return call();
			} catch (Exception e) {
				Optional.ofNullable(onFailure).ifPresent(consumer -> consumer.accept(e));
				return call(callable);
			}
		}

		private V andTry(Consumer<Optional<Object>> onSuccess, Callable<V> callable) throws Exception {
			V value = call();
			Optional.ofNullable(onSuccess).ifPresent(consumer -> consumer.accept(Optional.ofNullable(value)));
			return call(callable);
		}
	}
}