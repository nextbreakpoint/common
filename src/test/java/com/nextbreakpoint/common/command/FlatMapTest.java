package com.nextbreakpoint.common.command;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FlatMapTest {
	@Test
	void shouldThrowNullPointerExceptionWhenFunctionInNull() {
		assertThatThrownBy(() -> Command.value("X").flatMap(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void shouldNotCallFunctionWhenFailure() {
		Function<Object, Command<Object>> function = mock(Function.class);
		Command.error(new Exception()).flatMap(function).execute();
		verify(function, never()).apply(any());
	}

	@Test
	void shouldCallFunctionWhenSuccessAndValueIsNull() {
		Function<Object, Command<Object>> function = mock(Function.class);
		when(function.apply(null)).thenReturn(Command.value("Y"));
		Command.value(null).flatMap(function).execute();
		verify(function, times(1)).apply(isNull());
	}

	@Test
	void shouldCallFunctionWhenSuccessAndValueIsNotNull() {
		Function<String, Command<Object>> function = mock(Function.class);
		when(function.apply("X")).thenReturn(Command.value("Y"));
		Command.value("X").flatMap(function).execute();
		verify(function, times(1)).apply("X");
	}

	@Test
	void shouldReturnFailureWhenFunctionThrowsException() {
		Function<String, Command<Object>> function = mock(Function.class);
        when(function.apply(any())).thenThrow(RuntimeException.class);
		assertThat(Command.value("X").flatMap(function).execute().isFailure()).isTrue();
		verify(function, times(1)).apply("X");
	}

	@Test
	void shouldReturnExceptionWhenFunctionThrowsException() {
		Function<String, Command<Object>> function = mock(Function.class);
		final RuntimeException exception = new RuntimeException("Error");
		when(function.apply(any())).thenThrow(exception);
		assertThat(Command.value("X").flatMap(function).execute().exception()).isEqualTo(exception);
		verify(function, times(1)).apply("X");
	}

	@Test
	void shouldReturnFailureWhenFunctionReturnsNull() {
		Function<String, Command<Object>> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		assertThat(Command.value("X").flatMap(function).execute().isFailure()).isTrue();
		assertThat(Command.value("X").flatMap(function).execute().exception()).isInstanceOf(NullPointerException.class);
		verify(function, times(2)).apply("X");
	}
}
