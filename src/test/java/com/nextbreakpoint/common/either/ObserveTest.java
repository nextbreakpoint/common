package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

 class ObserveTest {
	@Test
	 void shouldInvokeOnSuccessWithNullValueWhenSuccessIsNull() {
		final Consumer<String> valueConsumer = mock(Consumer.class);
		final Consumer<Exception> errorConsumer = mock(Consumer.class);
		assertThat(Either.<String>success(null).observe().onSuccess(valueConsumer).onFailure(errorConsumer).get()).isNotNull();
		verify(valueConsumer, times(1)).accept(null);
		verify(errorConsumer, never()).accept(any());
	}

	@Test
	 void shouldInvokeOnSuccessWithNotNullValueWhenSuccessIotNull() {
		final Consumer<String> valueConsumer = mock(Consumer.class);
		final Consumer<Exception> errorConsumer = mock(Consumer.class);
		assertThat(Either.success("X").observe().onSuccess(valueConsumer).onFailure(errorConsumer).get()).isNotNull();
		verify(valueConsumer, times(1)).accept("X");
		verify(errorConsumer, never()).accept(any());
	}

	@Test
	 void shouldInvokeOnFailureValueWhenFailure() {
		final Exception exception = new Exception();
		final Consumer<String> valueConsumer = mock(Consumer.class);
		final Consumer<Exception> errorConsumer = mock(Consumer.class);
		assertThat(Either.<String>failure(exception).observe().onSuccess(valueConsumer).onFailure(errorConsumer).get()).isNotNull();
		verify(valueConsumer, never()).accept(any());
		verify(errorConsumer, times(1)).accept(exception);
	}
}
