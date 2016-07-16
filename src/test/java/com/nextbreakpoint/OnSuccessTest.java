package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OnSuccessTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldNotCallSuccessConsumerWhenFailure() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.failure(new Exception()).onSuccess(consumer).orElse(null);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void shouldCallSuccessConsumerWhenSuccessAndValueIsNull() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.success(null).onSuccess(consumer).orElse(null);
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldCallSuccessConsumerWhenSuccessAndValueIsNotNull() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.success("X").onSuccess(consumer).isPresent();
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldCallSuccessConsumerWhenCallableReturnsNull() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.of(() -> null).onSuccess(consumer).orElse(null);
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldCallSuccessConsumerWhenCallableReturnsValue() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.of(() -> "X").onSuccess(consumer).isPresent();
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldNotCallSuccessConsumerWhenCallableThrowsException() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onSuccess(consumer).isFailure();
		verify(consumer, times(0)).accept(anyObject());
	}
}
