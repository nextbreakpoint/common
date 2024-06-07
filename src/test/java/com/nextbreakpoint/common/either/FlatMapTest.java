
package com.nextbreakpoint.common.either;

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
		assertThatThrownBy(() -> Either.success("X").flatMap(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	 void shouldNotCallFunctionWhenFailure() {
		Function<Object, Either<Object>> function = mock(Function.class);
		Either.failure(new Exception()).flatMap(function).get();
		verify(function, never()).apply(any());
	}

	@Test
	 void shouldCallFunctionWhenSuccessAndValueIsNull() {
		Function<Object, Either<Object>> function = mock(Function.class);
		when(function.apply(null)).thenReturn(Either.success("Y"));
		Either.success(null).flatMap(function).get();
		verify(function, times(1)).apply(isNull());
	}

	@Test
	 void shouldCallFunctionWhenSuccessAndValueIsNotNull() {
		Function<String, Either<Object>> function = mock(Function.class);
		when(function.apply("X")).thenReturn(Either.success("Y"));
		Either.success("X").flatMap(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	 void shouldReturnFailureWhenFunctionThrowsException() {
		Function<String, Either<Object>> function = mock(Function.class);
        when(function.apply(any())).thenThrow(RuntimeException.class);
		assertThat(Either.success("X").flatMap(function).isFailure()).isTrue();
		verify(function, times(1)).apply("X");
	}

	@Test
	 void shouldReturnExceptionWhenFunctionThrowsException() {
		Function<String, Either<Object>> function = mock(Function.class);
		final RuntimeException exception = new RuntimeException("Error");
		when(function.apply(any())).thenThrow(exception);
		assertThat(Either.success("X").flatMap(function).exception()).isEqualTo(exception);
		verify(function, times(1)).apply("X");
	}

	@Test
	 void shouldReturnFailureWhenFunctionReturnsNull() {
		Function<String, Either<Object>> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		assertThat(Either.success("X").flatMap(function).isFailure()).isTrue();
		assertThat(Either.success("X").flatMap(function).exception()).isInstanceOf(NullPointerException.class);
		verify(function, times(2)).apply("X");
	}
}
