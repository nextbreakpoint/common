package com.nextbreakpoint;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ExecuteTest {
	@Test
	public void shouldReturnSuccessWhenCallableReturnsNotNull() {
		assertTrue(Try.of(() -> "X").execute().isSuccess());
	}

	@Test
	public void shouldReturnFailureWhenCallableThrowsException() {
		assertTrue(Try.of(() -> { throw  new Exception(); }).execute().isFailure());
	}

	@Test
	public void shouldInvokeCallable() throws Exception {
		Callable<String> callable = mock(Callable.class);
		when(callable.call()).thenReturn("X");
		Try.of(callable).execute();
		verify(callable, times(1)).call();
	}

	@Test
	public void shouldInvokeCallableOnceWhenInvokingTwoTerminalOperations() throws Exception {
		Callable<String> callable = mock(Callable.class);
		when(callable.call()).thenReturn("X");
		Try<String, Exception> tryCallable = Try.of(callable).execute();
		tryCallable.isPresent();
		tryCallable.get();
		verify(callable, times(1)).call();
	}

	@Test
	public void shouldInvokeCallableTwiceWhenInvokingTwoTerminalOperations() throws Exception {
		Callable<String> callable = mock(Callable.class);
		when(callable.call()).thenReturn("X");
		Try<String, Exception> tryCallable = Try.of(callable);
		tryCallable.isPresent();
		tryCallable.get();
		verify(callable, times(2)).call();
	}

	@Test
	public void shouldInvokeOnSuccessOnceWhenInvokingTwoTerminalOperations() throws Exception {
		Callable<String> callable = mock(Callable.class);
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		when(callable.call()).thenReturn("X");
		Try<String, Exception> tryCallable = Try.of(callable).onSuccess(consumer).execute();
		tryCallable.isPresent();
		tryCallable.get();
		verify(callable, times(1)).call();
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void shouldInvokeOnFailureOnceWhenInvokingTwoTerminalOperations() throws Exception {
		Callable<String> callable = mock(Callable.class);
		Consumer<Exception> consumer = mock(Consumer.class);
		when(callable.call()).thenThrow(new Exception());
		Try<String, Exception> tryCallable = Try.of(callable).onFailure(consumer).execute();
		tryCallable.isPresent();
		tryCallable.orElse(null);
		verify(callable, times(1)).call();
		verify(consumer, times(1)).accept(any());
	}
}
