package com.nextbreakpoint;

import org.junit.Test;

import java.util.function.Consumer;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class OnFailureTest {
	@Test
	public void shouldNotThrowExceptionWhenConsumerIsNull() {
		Try.success("X").onFailure(null).get();
	}

	@Test
	public void shouldCallFailureConsumerWhenFailure() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.failure(new Exception()).onFailure(consumer).isFailure();
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldNotCallFailureConsumerWhenSuccessAndValueIsNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.success(null).onFailure(consumer).orElse(null);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void shouldNotCallFailureConsumerWhenSuccessAndValueIsNotNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.success("X").onFailure(consumer).isPresent();
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void shouldCallFailureConsumerWhenCallableThrowsException() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onFailure(consumer).isFailure();
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldNotCallFailureConsumerWhenCallableReturnsNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> null).onFailure(consumer).orElse(null);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void shouldNotCallFailureConsumerWhenCallableReturnsValue() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> "X").onFailure(consumer).isPresent();
		verify(consumer, times(0)).accept(anyObject());
	}
}
