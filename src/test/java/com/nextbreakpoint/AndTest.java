package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AndTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldNotCallSecondCallableWhenFailure() throws Exception {
		Callable<Object> callable = mock(Callable.class);
		when(callable.call()).thenReturn("X");
		Try.failure(new Exception()).and(callable).isFailure();
		verify(callable, times(0)).call();
	}

	@Test
	public void shouldCallSecondCallableWhenSuccessAndValueIsNull() throws Exception {
		Callable<Object> callable = mock(Callable.class);
		Try.success(null).and(callable).isPresent();
		verify(callable, times(1)).call();
	}

	@Test
	public void shouldCallSecondCallableWhenSuccessAndValueIsNotNull() throws Exception {
		Callable<String> callable = mock(Callable.class);
		Try.success("X").and(callable).isPresent();
		verify(callable, times(1)).call();
	}

	@Test
	public void shouldCallSecondCallableWhenFirstCallableReturnsValue() throws Exception {
		Callable<String> callable = mock(Callable.class);
		Try.of(() -> "X").and(callable).isPresent();
		verify(callable, times(1)).call();
	}

	@Test
	public void shouldReturnValueOfSecondCallableWhenFirstCallableReturnsValueAndSecondCallableReturnsValue() {
		assertEquals("Y", Try.of(() -> "X").and(() -> "Y").get());
	}

	@Test
	public void shouldNotCallSecondCallableWhenFirstCallableThrowsException() throws Exception {
		Callable<Object> callable = mock(Callable.class);
		Try.of(() -> { throw new Exception(); }).and(callable).isPresent();
		verify(callable, times(0)).call();
	}

	@Test
	public void shouldNotReturnValueWhenFirstCallableThrowsExceptionAndSecondCallableReturnsValue() {
		assertFalse(Try.of(() -> { throw new Exception(); }).and(() -> "Y").isPresent());
	}

	@Test
	public void shouldReturnFailureWhenAllCallablesThrowException() {
		assertTrue(Try.of(() -> { throw new Exception(); }).and(() -> { throw new Exception(); }).isFailure());
	}

	@Test
	public void shouldNotCallConsumersWhenFirstCallableThrowsException() {
		Consumer<Optional<Object>> consumer1 = mock(Consumer.class);
		Consumer<Optional<Object>> consumer2 = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onSuccess(consumer1).and(() -> "X").onSuccess(consumer2).isPresent();
		verify(consumer1, times(0)).accept(any(Optional.class));
		verify(consumer2, times(0)).accept(any(Optional.class));
	}

	@Test
	public void shouldNotCallConsumerWhenSecondCallableThrowsException() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).and(() -> { throw new Exception(); }).onSuccess(consumer).isFailure();
		verify(consumer, times(0)).accept(any(Optional.class));
	}

	@Test
	public void shouldNotCallConsumersWhenFirstAndSecondCallableThrowException() {
		Consumer<Optional<Object>> consumer1 = mock(Consumer.class);
		Consumer<Optional<Object>> consumer2 = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onSuccess(consumer1)
				.and(() -> { throw new Exception(); }).onSuccess(consumer2).isFailure();
		verify(consumer1, times(0)).accept(any(Optional.class));
		verify(consumer2, times(0)).accept(any(Optional.class));
	}

	@Test
	public void shouldNotCallConsumerWhenFirstAndSecondCallablesThrowException() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onSuccess(consumer).and(() -> { throw new Exception(); }).isFailure();
		verify(consumer, times(0)).accept(any(Optional.class));
	}
}
