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

/**
 * Try implements a functional API for handling checked and unchecked exceptions.
 * 
 * @author Andrea Medeghini
 *
 * @param <V> the type of returned value
 * @param <E> the type of captured exception
 */
public abstract class Try<V, E extends Exception> {
    /**
     * The function to transform an exception into the expected exception.
     */
    protected final Function<Exception, E> mapper;

	/**
	 * The filter to apply when mapping values.
	 */
	protected final Predicate<Object> filter;

	/**
	 * The handler to call when result is success.
	 */
	protected final Consumer<Object> onSuccess;

	/**
	 * The handler to call when result is failure.
	 */
	protected final Consumer<Exception> onFailure;

	/**
	 * Throws exception if present.
	 * @throws E the captured exception
	 */
	public abstract void throwException() throws E;
	
	/**
	 * Returns true if exception occurred.
	 * @return true when exception if present 
	 */
	public abstract boolean isFailure();

	/**
	 * Checks value is present.
	 * @return true when value is present
	 */
	public abstract boolean isPresent();

	/**
	 * Consumes exception if present.
	 * @param consumer the consumer
	 */
	public abstract void ifFailure(Consumer<E> consumer);

	/**
	 * Consumes value if present.
	 * @param consumer the consumer
	 */
	public abstract void ifPresent(Consumer<V> consumer);

	/**
	 * Consumes value if present or throws exception if present.
	 * @param consumer the consumer
	 * @throws E the captured exception
	 */
	public abstract void ifPresentOrThrow(Consumer<V> consumer) throws E, E;

	/**
	 * Returns the current value. 
	 * Throws NoSuchElementException if value not present.
	 * @return the value
	 */
	public abstract V get();

	/**
	 * Returns the current value if present or returns a default value.
	 * @param value the default value
	 * @return the value
	 */
	public abstract V getOrElse(V value);
	
	/**
	 * Returns the current value if present or throws exception if present.
	 * Throws NoSuchElementException if value not present.
	 * @return the value
	 * @throws E the captured exception
	 */
	public abstract V getOrThrow() throws E;

	/**
	 * Returns the current value if present or throws exception if present or returns a default value.
	 * @param value the default value
	 * @return the value
	 * @throws E the captured exception
	 */
	public abstract V getOrThrow(V value) throws E;

	/**
	 * Returns optional of current value.
	 * @return the optional
	 */
	public abstract Optional<V> value();

	/**
	 * Maps value if present.
	 * @param func the function
	 * @param <R> the result type
	 * @return new instance with given function result type
	 */
	public abstract <R> Try<R, E> map(Function<V, R> func);

	/**
	 * Maps value if present.
	 * @param func the function
	 * @param <R> the result type
	 * @return new instance with given function result type
	 */
	public abstract <R> Try<R, E> flatMap(Function<V, Try<R, E>> func);

	/**
	 * Executes callable if exception is present.
	 * @param callable the callable
	 * @return new instance
	 */
	public abstract Try<V, E> or(Callable<V> callable);

	/**
	 * Consumes the current value if present.
	 * @param consumer the consumer
	 * @return new instance with given function result type
	 */
	public abstract Try<V, E> onSuccess(Consumer<Object> consumer);

	/**
	 * Consumes the current exception if present.
	 * @param consumer the consumer
	 * @return new instance with given function result type
	 */
	public abstract Try<V, E> onFailure(Consumer<Exception> consumer);

    /**
     * Creates new instance with given mapper.
     * @param mapper the mapper
	 * @param <X> the type of exception
     * @return new instance with given mapper
     */
	public abstract <X extends Exception> Try<V, X> withMapper(Function<Exception, X> mapper);

	/**
	 * Creates new instance with given filter.
	 * @param filter the filter
	 * @return new instance with given filter
	 */
	public abstract Try<V, E> filter(Predicate<Object> filter);

    /**
     * Creates new instance with given callable.
     * @param callable the callable
	 * @param <R> the result type
     * @return new instance with given callable
     */
    public static <R> Try<R, Exception> of(Callable<R> callable) {
		return new TryCallable<>(defaultMapper(), defaultFilter(), callable);
    }

	/**
     * Creates new instance with given exception.
     * @param exception the exception
	 * @param <R> the result type
     * @return new instance with given exception
     */
    public static <R> Try<R, Exception> failure(Exception exception) {
		return new TryFailure<>(defaultMapper(), defaultFilter(), exception);
	}

    /**
     * Creates new instance with given value.
     * @param value the value
	 * @param <R> the result type
     * @return new instance with given value
     */
    public static <R> Try<R, Exception> success(R value) {
		return new TrySuccess<>(defaultMapper(), defaultFilter(), value);
	}

	private Try(Function<Exception, E> mapper, Predicate<Object> filter, Consumer<Object> onSuccess, Consumer<Exception> onFailure) {
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

		public TryFailure(Function<Exception, E> mapper, Predicate<Object> filter, Consumer<Object> onSuccessHandler, Consumer<Exception> onFailureHandler, E exception) {
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

		public boolean isPresent() {
			notifyEvent();
			return false;
		}

		public void ifFailure(Consumer<E> consumer) {
			notifyEvent();
			consumer.accept(exception);
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

		public V getOrElse(V value) {
			notifyEvent();
			return value;
		}

		public V getOrThrow() throws E {
			notifyEvent();
			throw exception;
	    }
		
		public V getOrThrow(V value) throws E {
			notifyEvent();
			throw exception;
	    }
		
		public Optional<V> value() {
			notifyEvent();
			return Optional.empty();
		}

		public void throwException() throws E {
			notifyEvent();
			throw exception;
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			return new TryFailure<>(mapper, defaultFilter(), onSuccess, onFailure, exception);
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			return new TryFailure<>(mapper, defaultFilter(), onSuccess, onFailure, exception);
		}

		public Try<V, E> or(Callable<V> callable) {
			return new TryCallable<>(mapper, defaultFilter(), onSuccess, onFailure, callable);
		}

		public Try<V, E> onSuccess(Consumer<Object> consumer) {
			return new TryFailure<>(mapper, filter, consumer, onFailure, exception);
		}

		public Try<V, E> onFailure(Consumer<Exception> consumer) {
			return new TryFailure<>(mapper, filter, onSuccess, consumer, exception);
		}

		public <X extends Exception> Try<V, X> withMapper(Function<Exception, X> mapper) {
			return new TryFailure<>(mapper, filter, onSuccess, onFailure, mapper.apply(exception));
		}

		public Try<V, E> filter(Predicate<Object> filter) {
			return new TryFailure<>(mapper, filter, onSuccess, onFailure, exception);
		}

		private void notifyEvent() {
			Optional.ofNullable(onFailure).ifPresent(consumer -> consumer.accept(exception));
		}
	}

	private static class TrySuccess<V, E extends Exception> extends Try<V, E> {
		private final V value;

		public TrySuccess(Function<Exception, E> mapper, Predicate<Object> filter, Consumer<Object> onSuccessHandler, Consumer<Exception> onFailureHandler, V value) {
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

		public boolean isPresent() {
			return value().isPresent();
		}

		public void ifFailure(Consumer<E> consumer) {
			notifyEvent();
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

		public V getOrElse(V value) {
			return value().orElse(value);
		}

		public V getOrThrow() throws E {
	        return value().get();
	    }

		public V getOrThrow(V value) throws E {
	        return value().orElse(value);
	    }
		
		public Optional<V> value() {
			notifyEvent();
			return evaluate();
		}

		public void throwException() throws E {
			notifyEvent();
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			return new TryCallable<>(mapper, defaultFilter(), onSuccess, onFailure,
				() -> evaluate().map(v -> func.apply(v)).orElse(null));
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			return new TryCallable<>(mapper, defaultFilter(), onSuccess, onFailure,
				() -> evaluate().map(v -> func.apply(v)).orElseGet(() -> empty()).getOrThrow(null));
		}

		public Try<V, E> or(Callable<V> callable) {
			return new TrySuccess<>(mapper, defaultFilter(), onSuccess, onFailure, value);
		}

		public Try<V, E> onSuccess(Consumer<Object> consumer) {
			return new TrySuccess<>(mapper, filter, consumer, onFailure, value);
		}

		public Try<V, E> onFailure(Consumer<Exception> consumer) {
			return new TrySuccess<>(mapper, filter, onSuccess, consumer, value);
		}

		public <X extends Exception> Try<V, X> withMapper(Function<Exception, X> mapper) {
			return new TrySuccess<>(mapper, filter, onSuccess, onFailure, value);
		}

		public Try<V, E> filter(Predicate<Object> filter) {
			return new TrySuccess<>(mapper, filter, onSuccess, onFailure, value);
		}

		private Optional<V> evaluate() {
			return Optional.ofNullable(value).filter(filter);
		}

		private <R> TrySuccess<R, E> empty() {
			return new TrySuccess<>(mapper, filter, onSuccess, onFailure, null);
		}

		private void notifyEvent() {
			Optional.ofNullable(onSuccess).ifPresent(consumer -> consumer.accept(value));
		}
	}

	private static class TryCallable<V, E extends Exception> extends Try<V, E> {
		private final Callable<V> callable;

		public TryCallable(Function<Exception, E> mapper, Predicate<Object> filter, Consumer<Object> onSuccessHandler, Consumer<Exception> onFailureHandler, Callable<V> callable) {
			super(mapper, filter, onSuccessHandler, onFailureHandler);
			Objects.requireNonNull(callable);
			this.callable = callable;
		}

		public TryCallable(Function<Exception, E> mapper, Predicate<Object> filter, Callable<V> callable) {
			this(mapper, filter, null, null, callable);
		}

		public boolean isFailure() {
			return evaluate().isFailure();
		}

		public boolean isPresent() {
			return evaluate().isPresent();
		}

		public void ifFailure(Consumer<E> consumer) {
			evaluate().ifFailure(consumer);
		}

		public void ifPresent(Consumer<V> consumer) {
			evaluate().ifPresent(consumer);
		}

		public void ifPresentOrThrow(Consumer<V> consumer) throws E {
			evaluate().ifPresentOrThrow(consumer);
		}

		public V get() {
			return evaluate().get();
		}

		public V getOrElse(V value) {
			return evaluate().getOrElse(value);
		}

		public V getOrThrow() throws E {
			return evaluate().getOrThrow();
		}

		public V getOrThrow(V value) throws E {
			return evaluate().getOrThrow(value);
		}

		public Optional<V> value() {
			return evaluate().value();
		}

		public void throwException() throws E {
			evaluate().throwException();
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			return new TryCallable<>(mapper, defaultFilter(), onSuccess, onFailure,
				() -> call(callable).filter(filter).map(v -> func.apply(v)).orElse(null));
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			return new TryCallable<>(mapper, defaultFilter(), onSuccess, onFailure,
				() -> call(callable).filter(filter).map(v -> func.apply(v)).orElseGet(() -> empty()).getOrThrow(null));
		}

		public Try<V, E> or(Callable<V> orCallable) {
			return new TryCallable<>(mapper, defaultFilter(), onSuccess, onFailure,
				() -> call(callable, orCallable).filter(filter).orElse(null));
		}

		public Try<V, E> onSuccess(Consumer<Object> consumer) {
			return new TryCallable<>(mapper, filter, consumer, onFailure, callable);
		}

		public Try<V, E> onFailure(Consumer<Exception> consumer) {
			return new TryCallable<>(mapper, filter, onSuccess, consumer, callable);
		}

		public <X extends Exception> Try<V, X> withMapper(Function<Exception, X> mapper) {
			return new TryCallable<>(mapper, filter, onSuccess, onFailure, callable);
		}

		public Try<V, E> filter(Predicate<Object> filter) {
			return new TryCallable<>(mapper, filter, onSuccess, onFailure, callable);
		}

		private Try<V, E> evaluate() {
			try {
				return new TrySuccess<>(mapper, defaultFilter(), onSuccess, onFailure, call(callable).filter(filter).orElse(null));
			} catch (Exception e) {
				return new TryFailure<>(mapper, defaultFilter(), onSuccess, onFailure, mapper.apply(e));
			}
		}

		private <R> TrySuccess<R, E> empty() {
			return new TrySuccess<>(mapper, filter, onSuccess, onFailure, null);
		}

		private Optional<V> call(Callable<V> callable) throws Exception {
			return Optional.ofNullable(callable.call());
		}

		private Optional<V> call(Callable<V> callable, Callable<V> orCallable) throws Exception {
			try {
				return call(callable);
			} catch (Exception e) {
				return call(orCallable);
			}
		}
	}
}