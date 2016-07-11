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
public abstract class Try<V, E extends Throwable> {
    /**
     * The function to transform an exception into the expected exception.
     */
    protected final Function<Throwable, E> mapper;

	/**
	 * The filter to apply when mapping values.
	 */
	protected final Predicate<Object> filter;

	/**
	 * The handler to call when result is success.
	 */
	protected final Consumer<Object> onSuccessHandler;

	/**
	 * The handler to call when result is failure.
	 */
	protected final Consumer<Throwable> onFailureHandler;

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
     * Returns optional of current value.
     * @return the optional
     */
    public abstract Optional<V> value();

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
	public abstract Try<V, E> onFailure(Consumer<Throwable> consumer);

	/**
	 * Creates new instance with mapped exception.
	 * @param mapper the mapper
	 * @param <X> the exception type
	 * @return new instance with given exception type
	 */
	public abstract <X extends Throwable> Try<V, X> convert(Function<Throwable, X> mapper);

    /**
     * Creates new instance with given mapper and callable.
     * @param mapper the mapper
     * @param callable the callable
	 * @param <R> the result type
	 * @param <E> the exception type
     * @return new instance with given mapper and callable
     */
    public static <R, E extends Throwable> Try<R, E> of(Function<Throwable, E> mapper, Callable<R> callable) {
		return new TryCallable<>(mapper, defaultFilter(), callable);
    }

    /**
     * Creates new instance with given mapper and exception.
     * @param mapper the mapper
     * @param exception the exception
	 * @param <R> the result type
	 * @param <E> the exception type
     * @return new instance with given mapper and exception
     */
    public static <R, E extends Throwable> Try<R, E> failure(Function<Throwable, E> mapper, E exception) {
		return new TryFailure<>(mapper, defaultFilter(), exception);
	}

    /**
     * Creates new instance with given mapper and value.
     * @param mapper the mapper
     * @param value the value
	 * @param <R> the result type
	 * @param <E> the exception type
     * @return new instance with given mapper and value
     */
    public static <R, E extends Throwable> Try<R, E> success(Function<Throwable, E> mapper, R value) {
		return new TrySuccess<>(mapper, defaultFilter(), value);
	}

    /**
     * Creates new instance with given callable.
     * @param callable the callable
	 * @param <R> the result type
     * @return new instance with given callable
     */
    public static <R> Try<R, Throwable> of(Callable<R> callable) {
		return new TryCallable<>(defaultMapper(), defaultFilter(), callable);
    }

	/**
     * Creates new instance with given exception.
     * @param exception the exception
	 * @param <R> the result type
     * @return new instance with given exception
     */
    public static <R> Try<R, Throwable> failure(Throwable exception) {
		return new TryFailure<>(defaultMapper(), defaultFilter(), exception);
	}

    /**
     * Creates new instance with given value.
     * @param value the value
	 * @param <R> the result type
     * @return new instance with given value
     */
    public static <R> Try<R, Throwable> success(R value) {
		return new TrySuccess<>(defaultMapper(), defaultFilter(), value);
	}

	private Try(Function<Throwable, E> mapper, Predicate<Object> filter) {
		this(mapper, filter, null, null);
	}

	private Try(Function<Throwable, E> mapper, Predicate<Object> filter, Consumer<Object> onSuccessHandler, Consumer<Throwable> onFailureHandler) {
		Objects.requireNonNull(mapper);
		Objects.requireNonNull(filter);
		this.mapper = mapper;
		this.filter = filter;
		this.onSuccessHandler = onSuccessHandler;
		this.onFailureHandler = onFailureHandler;
	}

	private static Function<Throwable, Throwable> defaultMapper() {
		return x -> x;
	}

	private static Predicate<Object> defaultFilter() {
		return v -> v != null;
	}

	private static class TryFailure<V, E extends Throwable> extends Try<V, E> {
		private final E exception;

		public TryFailure(Function<Throwable, E> mapper, Predicate<Object> filter, Consumer<Object> onSuccessHandler, Consumer<Throwable> onFailureHandler, E exception) {
			super(mapper, filter, onSuccessHandler, onFailureHandler);
			Objects.requireNonNull(exception);
			this.exception = exception;
		}

		public TryFailure(Function<Throwable, E> mapper, Predicate<Object> filter, E exception) {
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
			throw new NoSuchElementException("TryFailure doesn't have any value");
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
			return new TryFailure<>(mapper, filter, onSuccessHandler, onFailureHandler, exception);
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			return new TryFailure<>(mapper, filter, onSuccessHandler, onFailureHandler, exception);
		}

		public Try<V, E> or(Callable<V> callable) {
			return new TryCallable<>(mapper, filter, onSuccessHandler, onFailureHandler, callable);
		}

		public Try<V, E> onSuccess(Consumer<Object> consumer) {
			return new TryFailure<>(mapper, filter, consumer, onFailureHandler, exception);
		}

		public Try<V, E> onFailure(Consumer<Throwable> consumer) {
			return new TryFailure<>(mapper, filter, onSuccessHandler, consumer, exception);
		}

		public <X extends Throwable> Try<V, X> convert(Function<Throwable, X> mapper) {
			return new TryFailure<>(mapper, filter, onSuccessHandler, onFailureHandler, mapper.apply(exception));
		}

		private void notifyEvent() {
			Optional.ofNullable(onFailureHandler).ifPresent(consumer -> consumer.accept(exception));
		}
	}

	private static class TrySuccess<V, E extends Throwable> extends Try<V, E> {
		private final V value;

		public TrySuccess(Function<Throwable, E> mapper, Predicate<Object> filter, Consumer<Object> onSuccessHandler, Consumer<Throwable> onFailureHandler, V value) {
			super(mapper, filter, onSuccessHandler, onFailureHandler);
			this.value = value;
		}

		public TrySuccess(Function<Throwable, E> mapper, Predicate<Object> filter, V value) {
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
			return Optional.ofNullable(value);
		}

		public void throwException() throws E {
			notifyEvent();
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			return new TryCallable<>(mapper, filter, onSuccessHandler, onFailureHandler, () -> Optional.ofNullable(value).map(v -> func.apply(v)).orElse(null));
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			return new TryCallable<>(mapper, filter, onSuccessHandler, onFailureHandler, () -> unwrap(Optional.ofNullable(value).map(v -> func.apply(value)).orElseGet(() -> new TryCallable<>(mapper, filter, onSuccessHandler, onFailureHandler, null))));
		}

		public Try<V, E> or(Callable<V> callable) {
			return this;
		}

		public Try<V, E> onSuccess(Consumer<Object> consumer) {
			return new TrySuccess<>(mapper, filter, consumer, onFailureHandler, value);
		}

		public Try<V, E> onFailure(Consumer<Throwable> consumer) {
			return new TrySuccess<>(mapper, filter, onSuccessHandler, consumer, value);
		}

		public <X extends Throwable> Try<V, X> convert(Function<Throwable, X> mapper) {
			return new TrySuccess<>(mapper, filter, onSuccessHandler, onFailureHandler, value);
		}

		private void notifyEvent() {
			Optional.ofNullable(onSuccessHandler).ifPresent(consumer -> consumer.accept(value));
		}

		private <R> R unwrap(Try<R, E> result) throws Exception {
			try {
				return result.getOrThrow(null);
			} catch (Throwable e) {
				throw new Exception(e);
			}
		}
	}

	private static class TryCallable<V, E extends Throwable> extends Try<V, E> {
		protected final Callable<V> callable;

		public TryCallable(Function<Throwable, E> mapper, Predicate<Object> filter, Consumer<Object> onSuccessHandler, Consumer<Throwable> onFailureHandler, Callable<V> callable) {
			super(mapper, filter, onSuccessHandler, onFailureHandler);
			Objects.requireNonNull(callable);
			this.callable = callable;
		}

		public TryCallable(Function<Throwable, E> mapper, Predicate<Object> filter, Callable<V> callable) {
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
			return new TryCallable<>(mapper, filter, onSuccessHandler, onFailureHandler, () -> func.apply(callable.call()));
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			return new TryCallable<>(mapper, filter, onSuccessHandler, onFailureHandler, () -> unwrap(func.apply(callable.call())));
		}

		public Try<V, E> or(Callable<V> callable) {
			return new TryCallable<>(mapper, filter, onSuccessHandler, onFailureHandler, () -> unwrap(evaluate(this.callable, callable)));
		}

		public Try<V, E> onSuccess(Consumer<Object> consumer) {
			return new TryCallable<>(mapper, filter, consumer, onFailureHandler, callable);
		}

		public Try<V, E> onFailure(Consumer<Throwable> consumer) {
			return new TryCallable<>(mapper, filter, onSuccessHandler, consumer, callable);
		}

		public <X extends Throwable> Try<V, X> convert(Function<Throwable, X> mapper) {
			return new TryCallable<V, X>(mapper, filter, onSuccessHandler, onFailureHandler, callable);
		}

		private Try<V, E> evaluate() {
			return evaluate(callable);
		}

		private <R> R unwrap(Try<R, E> result) throws Exception {
			try {
				return result.getOrThrow(null);
			} catch (Throwable e) {
				throw new Exception(e);
			}
		}

		private Try<V, E> evaluate(Callable<V> callable) {
			try {
				return new TrySuccess(mapper, filter, onSuccessHandler, onFailureHandler, callable.call());
			} catch (Exception e) {
				return new TryFailure(mapper, filter, onSuccessHandler, onFailureHandler, mapper.apply(e));
			}
		}

		private Try<V, E> evaluate(Callable<V> callable, Callable<V> orCallable) {
			try {
				return new TrySuccess(mapper, filter, onSuccessHandler, onFailureHandler, callable.call());
			} catch (Exception e) {
				return evaluate(orCallable);
			}
		}
	}
}