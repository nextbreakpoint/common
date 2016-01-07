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
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Try<V, E extends Throwable> {
    protected final Function<Exception, E> expFunc;  
    
	private Try(Function<Exception, E> expFunc) {
	    this.expFunc = expFunc;
	}

	public abstract Boolean isSuccess();

	public abstract Boolean isFailure();

	public abstract void throwException() throws E;
	
	public abstract void ifPresent(Consumer<V> c);

	public abstract void ifPresentOrThrow(Consumer<V> c) throws E;

	public abstract <R> Try<R, E> map(Function<V, R> func);

	public abstract <R> Try<R, E> flatMap(Function<V, Try<R, E>> func);

	public abstract boolean isPresent();

	public abstract V get();

    public V getOrElse(V value) {
        if (isPresent()) {
            return get();
        } else {
            return value;
        }
    }

    public Optional<V> value() {
        if (isPresent()) {
            return Optional.of(get());
        } else {
            return Optional.empty();
        }
    }

    public Try<V, E> orElseTry(Block<V> block) {
        if (!isPresent()) {
            try {
                return Try.success(expFunc, block.run());   
            } catch (Exception e) {
                return Try.failure(expFunc, expFunc.apply(e));
            }
        } else {
            return this;
        }
    }

    public V getOrThrow() throws E {
        if (isFailure()) {
            throwException();
        }
        return get();
    }

	public static <V, E extends Throwable> Try<V, E> empty(Function<Exception, E> expFunc) {
		return Try.success(expFunc, null);
	}

	public static <V, E extends Throwable> Try<V, E> ofNullable(Function<Exception, E> expFunc, V value) {
		return Try.success(expFunc, value);
	}

    public static <V, E extends Throwable> Try<V, E> of(Function<Exception, E> expFunc, V value) {
        Objects.requireNonNull(value);
        return Try.success(expFunc, value);
    }

    public static <V, E extends Throwable> Try<V, E> of(Function<Exception, E> expFunc, Block<V> block) {
        Objects.requireNonNull(block);
        try {
        	return Try.success(expFunc, block.run());
        } catch (Exception e) {
        	return Try.failure(expFunc, expFunc.apply(e));
        }
    }

    public static <V, E extends Throwable> Try<V, E> failure(Function<Exception, E> expFunc, E e) {
		return new Failure<>(expFunc, e);
	}

    public static <V, E extends Throwable> Try<V, E> success(Function<Exception, E> expFunc, V value) {
		return new Success<>(expFunc, value);
	}

	@FunctionalInterface
    public interface Block<R> {
        public R run() throws Exception;
    }
    
    private static class Failure<V, E extends Throwable> extends Try<V, E> {
		private E exception;

		public Failure(Function<Exception, E> expFunc, E e) {
		    super(expFunc);
			this.exception = e;
		}

		@Override
		public Boolean isSuccess() {
			return false;
		}

		@Override
		public Boolean isFailure() {
			return true;
		}

		@Override
		public void throwException() throws E {
			throw this.exception;
		}

		public <R> Try<R, E> map(Function<V, R> func) {
			return Try.failure(expFunc, exception);
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			return Try.failure(expFunc, exception);
		}

		public void ifPresent(Consumer<V> c) {
		}

		public void ifPresentOrThrow(Consumer<V> c) throws E {
			throw exception;
		}
		
		public boolean isPresent() {
			return false;
		}

		public V get() {
			throw new NoSuchElementException();
		}
	}

	private static class Success<V, E extends Throwable> extends Try<V, E> {
		private V value;

		public Success(Function<Exception, E> expFunc, V value) {
            super(expFunc);
			this.value = value;
		}

		@Override
		public Boolean isSuccess() {
			return true;
		}

		@Override
		public Boolean isFailure() {
			return false;
		}

		@Override
		public void throwException() {
		}
		
		public <R> Try<R, E> map(Function<V, R> func) {
			try {
				return Try.ofNullable(expFunc, func.apply(value));
			} catch (Exception e) {
				return Try.failure(expFunc, expFunc.apply(e));
			}
		}

		public <R> Try<R, E> flatMap(Function<V, Try<R, E>> func) {
			try {
				if (value != null) {
					return func.apply(value);
				} else {
					return Try.empty(expFunc);
				}
			} catch (Exception e) {
				return Try.failure(expFunc, expFunc.apply(e));
			}
		}

		public void ifPresent(Consumer<V> c) {
			if (value != null) c.accept(value);
		}

		public void ifPresentOrThrow(Consumer<V> c) {
			if (value != null) c.accept(value);
		}

		public boolean isPresent() {
			return value != null;
		}

		public V get() {
			if (value == null) {
				throw new NoSuchElementException();
			}
			return value;
		}
	}
}