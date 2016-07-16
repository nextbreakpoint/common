package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class FilterTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldNotCallFilterWhenMapFailure() {
		Function<Object, Object> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn("Y");
		when(filter.test("X")).thenReturn(true);
		Try.failure(new Exception()).filter(filter).map(function).isFailure();
		verify(filter, times(0)).test("X");
	}

	@Test
	public void shouldNotCallFilterWhenFlatMapFailure() {
		Function<Object, Try<Object, Exception>> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn(Try.success("Y"));
		when(filter.test("X")).thenReturn(true);
		Try.failure(new Exception()).filter(filter).flatMap(function).isFailure();
		verify(filter, times(0)).test("X");
	}

	@Test
	public void shouldCallFilterWhenMapSuccessAndValueIsNotNull() {
		Function<String, Object> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn("Y");
		when(filter.test("X")).thenReturn(true);
		Try.success("X").filter(filter).map(function).get();
		verify(filter, times(1)).test("X");
	}

	@Test
	public void shouldCallFilterWhenFlatMapSuccessAndValueIsNotNull() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn(Try.success("Y"));
		when(filter.test("X")).thenReturn(true);
		Try.success("X").filter(filter).flatMap(function).get();
		verify(filter, times(1)).test("X");
	}

	@Test
	public void shouldCallFunctionWhenFilterReturnsTrue() {
		Function<String, Object> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn("Y");
		when(filter.test("X")).thenReturn(true);
		Try.success("X").filter(filter).map(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void shouldCallFilterWhenValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(true);
		Try.success("X").filter(filter).get();
		verify(filter, times(1)).test("X");
	}

	@Test
	public void shouldNotReturnValueWhenFilterReturnsFalseAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(false);
		assertFalse(Try.success("X").filter(filter).isPresent());
	}

	@Test
	public void shouldHaveValueWhenFilterReturnsTrueAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertTrue(Try.success("X").map(v -> v.toLowerCase()).filter(filter).isPresent());
	}

	@Test
	public void shouldNotHaveValueWhenFilterReturnsFalseBeforeMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertFalse(Try.success("X").filter(filter).map(v -> v.toLowerCase()).isPresent());
	}

	@Test
	public void shouldNotHaveValueWhenFilterReturnsFalseAfterMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(true);
		assertFalse(Try.success("X").map(v -> v.toLowerCase()).filter(filter).isPresent());
	}

	@Test
	public void shouldReturnValueWhenFilterIsAfterMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertEquals(Try.success("X").map(v -> v.toLowerCase()).filter(filter).get(), "x");
	}

	@Test
	public void shouldReturnValueWhenFilterIsBeforeMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(true);
		assertEquals(Try.success("X").filter(filter).map(v -> v.toLowerCase()).get(), "x");
	}

	@Test
	public void shouldCallFilterWhenCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(true);
		Try.of(() -> "X").filter(filter).get();
		verify(filter, times(1)).test("X");
	}

	@Test
	public void shouldNotReturnValueWhenFilterReturnsFalseAndCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(false);
		assertFalse(Try.of(() -> "X").filter(filter).isPresent());
	}

	@Test
	public void shouldHaveValueWhenFilterReturnsTrueAndCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertTrue(Try.of(() -> "X").map(v -> v.toLowerCase()).filter(filter).isPresent());
	}

	@Test
	public void shouldNotHaveValueWhenFilterReturnsFalseBeforeMapAndCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertFalse(Try.of(() -> "X").filter(filter).map(v -> v.toLowerCase()).isPresent());
	}

	@Test
	public void shouldNotHaveValueWhenFilterReturnsFalseAfterMapAndCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(true);
		assertFalse(Try.of(() -> "X").map(v -> v.toLowerCase()).filter(filter).isPresent());
	}

	@Test
	public void shouldReturnValueWhenFilterIsAfterMapAndCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertEquals(Try.of(() -> "X").map(v -> v.toLowerCase()).filter(filter).get(), "x");
	}

	@Test
	public void shouldReturnValueWhenFilterIsBeforeMapAndCallableReturnsValue() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(true);
		assertEquals(Try.of(() -> "X").filter(filter).map(v -> v.toLowerCase()).get(), "x");
	}
}
