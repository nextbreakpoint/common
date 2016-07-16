package com.nextbreakpoint;

import org.junit.Test;

import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class IfSuccessTest {
	@Test
	public void shouldNotCallConsumerWhenFailure() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifSuccess(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldCallConsumerWhenSuccessAndValueIsNull() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.success(null).ifSuccess(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void shouldCallConsumerWhenSuccessAndValueIsNotNull() {
		Consumer<Optional<String>> consumer = mock(Consumer.class);
		Try.success("X").ifSuccess(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void shouldNotCallConsumerWhenCallableThrowsException() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).ifSuccess(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldCallConsumerWhenCallableReturnsNull() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.of(() -> null).ifSuccess(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void shouldCallConsumerWhenCallableReturnsValue() {
		Consumer<Optional<String>> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifSuccess(consumer);
		verify(consumer, times(1)).accept(any());
	}
}
