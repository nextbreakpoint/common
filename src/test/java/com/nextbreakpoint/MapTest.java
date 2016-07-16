package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MapTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldNotCallFunctionWhenFailure() {
		Function<Object, Object> function = mock(Function.class);
		Try.failure(new Exception()).map(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldNotCallFunctionWhenSuccessAndValueIsNull() {
		Function<Object, Object> function = mock(Function.class);
		when(function.apply(null)).thenReturn("Y");
		Try.success(null).map(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldCallFunctionWhenSuccessAndValueIsNotNull() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply("X")).thenReturn("Y");
		Try.success("X").map(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void shouldReturnFailureWhenSuccessAndFunctionThrowsException() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.success("X").map(function).isFailure());
	}

	@Test
	public void shouldThrowNoSuchElementExceptionWhenFunctionReturnsNull() {
		exception.expect(NoSuchElementException.class);
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		Try.success("X").map(function).get();
	}

	@Test
	public void shouldNotCallFunctionWhenCallableReturnsNull() {
		Function<Object, Object> function = mock(Function.class);
		Try.of(() -> null).map(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldCallFunctionWhenCallableReturnsValue() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply("X")).thenReturn("Y");
		Try.of(() -> "X").map(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void shouldReturnFailureWhenCallableRetunsValueAndFunctionThrowsException() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.of(() -> "X").map(function).isFailure());
	}

	@Test
	public void shouldNotReturnFailureWhenCallableRetunsValueAndFunctionReturnsNull() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		assertFalse(Try.of(() -> "X").map(function).isFailure());
	}

	@Test
	public void shouldNotCallFunctionWhenCallableThrowsException() {
		Function<Object, Object> function = mock(Function.class);
		Try.of(() -> { throw new Exception(); }).map(function).orElse(null);
		verify(function, times(0)).apply(any());
	}
}
