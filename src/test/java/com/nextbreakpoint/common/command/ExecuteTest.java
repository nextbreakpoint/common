package com.nextbreakpoint.common.command;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExecuteTest {
	@Test
	void shouldInvokeCallable() throws Exception {
		Callable<String> callable = mock(Callable.class);
		when(callable.call()).thenReturn("X");
		Command.of(callable).execute();
		verify(callable, times(1)).call();
	}

	@Test
	void shouldReturnSuccessWhenCallableReturnsANullValue() {
		assertThat(Command.of(() -> null).execute().isSuccess()).isTrue();
	}

	@Test
	void shouldReturnSuccessWhenCallableReturnsANotNullValue() {
		assertThat(Command.of(() -> "X").execute().isSuccess()).isTrue();
	}

	@Test
	void shouldReturnFailureWhenCallableThrowsAnException() {
		assertThat(Command.of(() -> { throw new Exception(); }).execute().isFailure()).isTrue();
	}

	@Test
	void shouldReturnValueProducedByCallable() {
		assertThat(Command.of(() -> "X").execute().get()).isEqualTo("X");
	}
}
