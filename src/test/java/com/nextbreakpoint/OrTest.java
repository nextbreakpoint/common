package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class OrTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldCallSecondCallableWhenFailure() throws Exception {
		Callable<Object> callable = mock(Callable.class);
		when(callable.call()).thenReturn("X");
		Try.failure(new Exception()).or(callable).isFailure();
		verify(callable, times(1)).call();
	}

	@Test
	public void shouldNotCallSecondCallableWhenSuccessAndValueIsNull() throws Exception {
		Callable<Object> callable = mock(Callable.class);
		Try.success(null).or(callable).isPresent();
		verify(callable, times(0)).call();
	}

	@Test
	public void shouldNotCallSecondCallableWhenSuccessAndValueIsNotNull() throws Exception {
		Callable<String> callable = mock(Callable.class);
		Try.success("X").or(callable).isPresent();
		verify(callable, times(0)).call();
	}

	@Test
	public void shouldNotCallSecondCallableWhenFirstCallableReturnsValue() throws Exception {
		Callable<String> callable = mock(Callable.class);
		Try.of(() -> "X").or(callable).isPresent();
		verify(callable, times(0)).call();
	}

	@Test
	public void shouldReturnValueOfFirstCallableWhenFirstCallableReturnsValueAndSecondCallableReturnsValue() {
		assertEquals("X", Try.of(() -> "X").or(() -> "Y").get());
	}

	@Test
	public void shouldCallSecondCallableWhenFirstCallableThrowsException() throws Exception {
		Callable<Object> callable = mock(Callable.class);
		Try.of(() -> { throw new Exception(); }).or(callable).isPresent();
		verify(callable, times(1)).call();
	}

	@Test
	public void shouldReturnValueOfSecondCallableWhenFirstCallableThrowsExceptionAndSecondCallableReturnsValue() {
		assertEquals("Y", Try.of(() -> { throw new Exception(); }).or(() -> "Y").get());
	}

	@Test
	public void shouldReturnFailureWhenAllCallablesThrowException() {
		assertTrue(Try.of(() -> { throw new Exception(); }).or(() -> { throw new Exception(); }).isFailure());
	}

	@Test
	public void shouldCallConsumerWhenFirstCallableThrowsException() {
		Consumer<Exception> consumer1 = mock(Consumer.class);
		Consumer<Exception> consumer2 = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onFailure(consumer1).or(() -> "X").onFailure(consumer2).isPresent();
		verify(consumer1, times(1)).accept(any(Exception.class));
		verify(consumer2, times(0)).accept(any(Exception.class));
	}

	@Test
	public void shouldCallConsumerWhenSecondCallableThrowsException() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).or(() -> { throw new Exception(); }).onFailure(consumer).isFailure();
		verify(consumer, times(1)).accept(any(Exception.class));
	}

	@Test
	public void shouldCallConsumersWhenFirstAndSecondCallableThrowException() {
		Consumer<Exception> consumer1 = mock(Consumer.class);
		Consumer<Exception> consumer2 = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onFailure(consumer1)
				.or(() -> { throw new Exception(); }).onFailure(consumer2).isFailure();
		verify(consumer1, times(1)).accept(any(Exception.class));
		verify(consumer2, times(1)).accept(any(Exception.class));
	}

	@Test
	public void shouldCallConsumerTwoTimesWhenFirstAndSecondCallableThrowsException() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onFailure(consumer).or(() -> { throw new Exception(); }).isFailure();
		verify(consumer, times(2)).accept(any(Exception.class));
	}
}
