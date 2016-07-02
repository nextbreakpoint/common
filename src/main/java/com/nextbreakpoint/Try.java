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
	 * Throws exception if present.
	 * @throws E the captured exception
	 */
	public void throwException() throws E {}
	
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
	 * Consumes value if present.
	 * @param c the consumer
	 */
	public abstract void ifPresent(Consumer<V> c);

	/**
	 * Consumes value if present or throws exception if present.
	 * @param c the consumer
	 * @throws E the captured exception
	 */
	public abstract void ifPresentOrThrow(Consumer<V> c) throws E;

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
	 * Consumes exception if present.
	 */
	public abstract Try<V, E> ifFailure(Consumer<E> consumer);

	/**
	 * Consumes the current value if present.
	 */
	public Try<V, E> peek(Consumer<V> consumer) {
		if (isPresent()) {
			consumer.accept(get());
		}
		return this;
	}

    /**
     * Creates new instance with given mapper and callable.
     * @param mapper the mapper
     * @param callable the callable
	 * @param <R> the result type
	 * @param <E> the exception type
     * @return new instance with given mapper and callable
     */
    public static <R, E extends Throwable> Try<R, E> of(Function<Throwable, E> mapper, Callable<R> callable) {
        Objects.requireNonNull(callable);
        try {
        	return Try.success(mapper, callable.call());
        } catch (Exception e) {
        	return Try.failure(mapper, mapper.apply(e));
        }
    }

    /**
     * Creates new instance with given mapper and exception.
     * @param mapper the mapper
     * @param e the exception
	 * @param <R> the result type
	 * @param <E> the exception type
     * @return new instance with given mapper and exception
     */
    public static <R, E extends Throwable> Try<R, E> failure(Function<Throwable, E> mapper, E e) {
		return new Failure<>(mapper, e);
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
		return new Success<>(mapper, value);
	}

    /**
     * Creates new instance with given callable.
     * @param callable the callable
	 * @param <R> the result type
     * @return new instance with given callable
     */
    public static <R> Try<R, Throwable> of(Callable<R> callable) {
        Objects.requireNonNull(callable);
        try {
        	return Try.success(defaultMapper(), callable.call());
        } catch (Exception e) {
        	return Try.failure(defaultMapper(), e);
        }
    }

    /**
     * Creates new instance with given exception.
     * @param e the exception
	 * @param <R> the result type
     * @return new instance with given exception
     */
    public static <R> Try<R, Throwable> failure(Throwable e) {
		return new Failure<>(defaultMapper(), e);
	}

    /**
     * Creates new instance with given value.
     * @param value the value
	 * @param <R> the result type
     * @return new instance with given value
     */
    public static <R> Try<R, Throwable> success(R value) {
		return new Success<>(defaultMapper(), value);
	}

	private Try(Function<Throwable, E> mapper) {
        Objects.requireNonNull(mapper);
	    this.mapper = mapper;
	}

	private static Function<Throwable, Throwable> defaultMapper() {
		return x -> x;
	}

	private static class Failure<V, E extends Throwable> extends Try<V, E> {
		private E exception;

		public Failure(Function<Throwable, E> mapper, E e) {
		    super(mapper);
		    Objects.requireNonNull(e);
			this.exception = e;
		}

		@Override
		public boolean isFailure() {
			return true;
		}

		@Override
		public void throwException() throws E {
			throw this.exception;
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			return Try.failure(mapper, exception);
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			return Try.failure(mapper, exception);
		}
		
	    public Try<V, E> or(Callable<V> callable) {
            try {
                return Try.success(mapper, callable.call());   
            } catch (Exception e) {
                return Try.failure(mapper, mapper.apply(e));
            }
	    }

		public boolean isPresent() {
			return false;
		}

		public void ifPresent(Consumer<V> c) {
		}

		public void ifPresentOrThrow(Consumer<V> c) throws E {
			throw exception;
		}

		public V get() {
			throw new NoSuchElementException("Failure doesn't have any value");
		}

		public V getOrElse(V value) {
			return value;
		}

		public V getOrThrow() throws E {
			throw exception;
	    }
		
		public V getOrThrow(V value) throws E {
			throw exception;
	    }
		
		@Override
		public Optional<V> value() {
			return Optional.empty();
		}

		public Try<V, E> ifFailure(Consumer<E> consumer) {
			consumer.accept(exception);
			return this;
		}
	}

	private static class Success<V, E extends Throwable> extends Try<V, E> {
		private final V value;

		public Success(Function<Throwable, E> mapper, V value) {
            super(mapper);
			this.value = value;
		}

		@Override
		public boolean isFailure() {
			return false;
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			try {
				return value().map(value -> Try.success(mapper, func.apply(value))).orElseGet(() -> Try.success(mapper, null));
			} catch (Exception e) {
				return Try.failure(mapper, mapper.apply(e));
			}
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			try {
				return value().map(value -> func.apply(value)).orElseGet(() -> Try.success(mapper, null));
			} catch (Exception e) {
				return Try.failure(mapper, mapper.apply(e));
			}
		}
		
	    public Try<V, E> or(Callable<V> callable) {
            return this;
	    }

		public boolean isPresent() {
			return value().isPresent();
		}

		public void ifPresent(Consumer<V> c) {
			value().ifPresent(c);
		}

		public void ifPresentOrThrow(Consumer<V> c) {
			value().ifPresent(c);
		}

		public V get() {
			return value().get();
		}

		public V getOrElse(V value) {
			return value().orElse(value);
		}

		public V getOrThrow() throws E {
	        return get();
	    }

		public V getOrThrow(V value) throws E {
	        return getOrElse(value);
	    }
		
		@Override
		public Optional<V> value() {
			return Optional.ofNullable(value);
		}

		public Try<V, E> ifFailure(Consumer<E> consumer) {
			return this;
		}
	}
}