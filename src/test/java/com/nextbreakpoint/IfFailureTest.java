package com.nextbreakpoint;

import org.junit.Test;

import java.util.function.Consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class IfFailureTest {
	@Test
	public void shouldCallConsumerWhenFailure() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifFailure(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void shouldNotCallConsumerWhenSuccessAndValueIsNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.success(null).ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldNotCallConsumerWhenSuccessAndValueIsNotNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.success("X").ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldCallConsumerWhenCallableThrowsException() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).ifFailure(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void shouldNotCallConsumerWhenCallableReturnsNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> null).ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldNotCallConsumerWhenCallableReturnsValue() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}
}
