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

public abstract class Try<V, E extends Throwable> {
    protected final Function<Throwable, E> mapper;  
    
	private Try(Function<Throwable, E> mapper) {
        Objects.requireNonNull(mapper);
	    this.mapper = mapper;
	}

	public void throwException() throws E {}
	
	public abstract boolean isFailure();

	public abstract boolean isPresent();

	public abstract void ifPresent(Consumer<V> c);

	public abstract void ifPresentOrThrow(Consumer<V> c) throws E;

	public abstract <R> Try<R, E> map(Function<V, R> func);

	public abstract <R> Try<R, E> flatMap(Function<V, Try<R, E>> func);

    public abstract Try<V, E> or(Callable<V> block);

    public abstract Optional<V> value();

	public abstract V get();

	public abstract V getOrElse(V value);
	
	public abstract V getOrThrow() throws E;

    public static <V, E extends Throwable> Try<V, E> of(Function<Throwable, E> mapper, Callable<V> callable) {
        Objects.requireNonNull(callable);
        try {
        	return Try.success(mapper, callable.call());
        } catch (Exception e) {
        	return Try.failure(mapper, mapper.apply(e));
        }
    }

    public static <V, E extends Throwable> Try<V, E> failure(Function<Throwable, E> mapper, E e) {
		return new Failure<>(mapper, e);
	}

    public static <V, E extends Throwable> Try<V, E> success(Function<Throwable, E> mapper, V value) {
		return new Success<>(mapper, value);
	}

    public static <V> Try<V, Throwable> of(Callable<V> callable) {
        Objects.requireNonNull(callable);
        try {
        	return Try.success(defaultMapper(), callable.call());
        } catch (Exception e) {
        	return Try.failure(defaultMapper(), e);
        }
    }

    public static <V> Try<V, Throwable> failure(Throwable e) {
		return new Failure<>(defaultMapper(), e);
	}

    public static <V> Try<V, Throwable> success(V value) {
		return new Success<>(defaultMapper(), value);
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
		
		@Override
		public Optional<V> value() {
			return Optional.empty();
		}
	}

	private static class Success<V, E extends Throwable> extends Try<V, E> {
		private final Optional<V> optional;

		public Success(Function<Throwable, E> mapper, V value) {
            super(mapper);
			this.optional = Optional.ofNullable(value);
		}

		@Override
		public boolean isFailure() {
			return false;
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			try {
				return optional.map(value -> Try.success(mapper, func.apply(value))).orElseGet(() -> Try.success(mapper, null));
			} catch (Exception e) {
				return Try.failure(mapper, mapper.apply(e));
			}
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			try {
				return optional.map(value -> func.apply(value)).orElseGet(() -> Try.success(mapper, null));
			} catch (Exception e) {
				return Try.failure(mapper, mapper.apply(e));
			}
		}
		
	    public Try<V, E> or(Callable<V> callable) {
            return this;
	    }

		public boolean isPresent() {
			return optional.isPresent();
		}

		public void ifPresent(Consumer<V> c) {
			optional.ifPresent(c);
		}

		public void ifPresentOrThrow(Consumer<V> c) {
			optional.ifPresent(c);
		}

		public V get() {
			return optional.get();
		}

		public V getOrElse(V value) {
			return optional.orElse(value);
		}

		public V getOrThrow() throws E {
	        return get();
	    }
		
		@Override
		public Optional<V> value() {
			return optional;
		}
	}
}