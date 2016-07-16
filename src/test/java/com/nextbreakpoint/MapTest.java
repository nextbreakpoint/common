package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MapTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowNullPointerExceptionWhenFunctionInNull() {
		exception.expect(NullPointerException.class);
		Try.success("X").map(null);
	}

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
	public void shouldNotCallFunctionWhenCallableThrowsException() {
		Function<Object, Object> function = mock(Function.class);
		Try.of(() -> { throw new Exception(); }).map(function).orElse(null);
		verify(function, times(0)).apply(any());
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
	public void shouldReturnFailureWhenFunctionThrowsException() {
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
	public void shouldHaveValueWhenFilterReturnsTrueAndFilterIsAfterMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertTrue(Try.success("X").map(v -> v.toLowerCase()).filter(filter).isPresent());
	}

	@Test
	public void shouldNotHaveValueWhenFilterReturnsFalseAndFilterIsBeforeMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(false);
		assertFalse(Try.success("X").filter(filter).map(v -> v.toLowerCase()).isPresent());
	}

	@Test
	public void shouldHaveValueWhenFilterReturnsTrueAndFilterIsAfterMapAndCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertTrue(Try.of(() -> "X").map(v -> v.toLowerCase()).filter(filter).isPresent());
	}

	@Test
	public void shouldNotHaveValueWhenFilterReturnsFalseAndFilterIsBeforeMapAndCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(false);
		assertFalse(Try.of(() -> "X").filter(filter).map(v -> v.toLowerCase()).isPresent());
	}
}
