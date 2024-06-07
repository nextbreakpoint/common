package com.nextbreakpoint.common.command;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MapTest {
	@Test
	void shouldThrowNullPointerExceptionWhenFunctionInNull() {
		assertThatThrownBy(() -> Command.value("X").map(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void shouldNotCallFunctionWhenFailure() {
		Function<Object, Object> function = mock(Function.class);
		Command.error(new Exception()).map(function).execute();
		verify(function, never()).apply(any());
	}

	@Test
	void shouldCallFunctionWhenSuccessAndValueIsNull() {
		Function<Object, Object> function = mock(Function.class);
		when(function.apply(null)).thenReturn("Y");
		Command.value(null).map(function).execute();
		verify(function, times(1)).apply(any());
	}

	@Test
	void shouldCallFunctionWhenSuccessAndValueIsNotNull() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply("X")).thenReturn("Y");
		Command.value("X").map(function).execute();
		verify(function, times(1)).apply("X");
	}

	@Test
	void shouldReturnFailureWhenFunctionThrowsException() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertThat(Command.value("X").map(function).execute().isFailure()).isTrue();
	}

	@Test
	void shouldReturnExceptionWhenFunctionThrowsException() {
		Function<String, Object> function = mock(Function.class);
		final RuntimeException exception = new RuntimeException("Error");
		when(function.apply(any())).thenThrow(exception);
		assertThat(Command.value("X").map(function).execute().exception()).isEqualTo(exception);
	}

	@Test
	void shouldReturnSuccessWhenFunctionReturnsNull() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		assertThat(Command.value("X").map(function).execute().isSuccess()).isTrue();
	}
}
