package com.nextbreakpoint;

import org.junit.Test;

import java.util.function.Consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class IfPresentTest {
	@Test
	public void shouldNotCallConsumerWhenFailure() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldNotCallConsumerWhenSuccessAndValueIsNull() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.success(null).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldCallConsumerWhenSuccessAndValueIsNotNull() {
		Consumer<String> consumer = mock(Consumer.class);
		Try.success("X").ifPresent(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void shouldNotCallConsumerWhenCallableThrowsException() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldNotCallConsumerWhenCallableReturnsNull() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldCallConsumerWhenCallableReturnsValue() {
		Consumer<String> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifPresent(consumer);
		verify(consumer, times(1)).accept(any());
	}
}
