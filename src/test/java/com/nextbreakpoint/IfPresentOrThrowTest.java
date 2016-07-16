package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class IfPresentOrThrowTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowExceptionWhenFailure() throws Exception {
		exception.expect(Exception.class);
		Consumer<Object> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifPresentOrThrow(consumer);
	}

	@Test
	public void shouldNotCallConsumerWhenSuccessAndValueIsNull() throws Exception {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.success(null).ifPresentOrThrow(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldCallConsumerWhenSuccessAndValueIsNotNull() throws Exception {
		Consumer<String> consumer = mock(Consumer.class);
		Try.success("X").ifPresentOrThrow(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void shouldNotCallConsumerWhenCallableReturnsNull() throws Exception {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).ifPresentOrThrow(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void shouldCallConsumerWhenCallableReturnsValue() throws Exception {
		Consumer<String> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifPresentOrThrow(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void shouldThrowExceptionWhenCallableThrowsException() throws Exception {
		exception.expect(Exception.class);
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).ifPresentOrThrow(consumer);
	}
}
