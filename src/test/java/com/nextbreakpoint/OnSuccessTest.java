package com.nextbreakpoint;

import org.junit.Test;

import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class OnSuccessTest {
	@Test
	public void shouldNotThrowExceptionWhenConsumerIsNull() {
		Try.success("X").onSuccess(null).get();
	}

	@Test
	public void shouldNotCallSuccessConsumerWhenFailure() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.failure(new Exception()).onSuccess(consumer).isFailure();
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
	public void shouldNotCallSuccessConsumerWhenCallableThrowsException() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onSuccess(consumer).isFailure();
		verify(consumer, times(0)).accept(anyObject());
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
}
