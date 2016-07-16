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

public class FlatMapTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowNullPointerExceptionWhenFunctionInNull() {
		exception.expect(NullPointerException.class);
		Try.success("X").flatMap(null);
	}

	@Test
	public void shouldNotCallFunctionWhenFailure() {
		Function<Object, Try<Object, Exception>> function = mock(Function.class);
		Try.failure(new Exception()).flatMap(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldNotCallFunctionWhenSuccessAndValueIsNull() {
		Function<Object, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply(null)).thenReturn(Try.success("Y"));
		Try.success(null).flatMap(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldCallFunctionWhenSuccessAndValueIsNotNull() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply("X")).thenReturn(Try.success("Y"));
		Try.success("X").flatMap(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void shouldNotCallFunctionWhenCallableThrowsException() {
		Function<Object, Try<Object, Exception>> function = mock(Function.class);
		Try.of(() -> { throw new Exception(); }).flatMap(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldNotCallFunctionWhenCallableReturnsNull() {
		Function<Object, Try<Object, Exception>> function = mock(Function.class);
		Try.of(() -> null).flatMap(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldCallFunctionWhenCallableReturnsValue() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply("X")).thenReturn(Try.of(() -> "Y"));
		Try.of(() -> "X").flatMap(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void shouldReturnFailureWhenFunctionThrowsException() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.success("X").flatMap(function).isFailure());
	}

	@Test
	public void shouldThrowNoSuchElementExceptionWhenFunctionReturnsNull() {
		exception.expect(NoSuchElementException.class);
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		Try.success("X").flatMap(function).get();
	}

	@Test
	public void shouldHaveValueWhenFilterReturnsTrueAndFilterIsAfterMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertTrue(Try.success("X").flatMap(v -> Try.success("x")).filter(filter).isPresent());
	}

	@Test
	public void shouldNotHaveValueWhenFilterReturnsFalseAndFilterIsBeforeMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(false);
		assertFalse(Try.success("X").filter(filter).flatMap(v -> Try.success("x")).isPresent());
	}

	@Test
	public void shouldHaveValueWhenFilterReturnsTrueAndFilterIsAfterMapAndCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertTrue(Try.of(() -> "X").flatMap(v -> Try.success("x")).filter(filter).isPresent());
	}

	@Test
	public void shouldNotHaveValueWhenFilterReturnsFalseAndFilterIsBeforeMapAndCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(false);
		assertFalse(Try.of(() -> "X").filter(filter).flatMap(v -> Try.success("x")).isPresent());
	}
}
