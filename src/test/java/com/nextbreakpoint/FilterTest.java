package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.mockito.Mockito.*;

public class FilterTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowNullPointerExceptionWhenFilterIsNull() {
		exception.expect(NullPointerException.class);
		Try.failure(new Exception()).filter(null);
	}

	@Test
	public void shouldNotCallWhenFailure() {
		Function<Object, Object> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn("Y");
		when(filter.test("X")).thenReturn(true);
		Try.failure(new Exception()).filter(filter).map(function).isFailure();
		verify(filter, times(0)).test("X");
	}

	@Test
	public void shouldCallFilterWhenValueIsNotNull() {
		Function<String, Object> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn("Y");
		when(filter.test("X")).thenReturn(true);
		Try.success("X").filter(filter).map(function).get();
		verify(filter, times(1)).test("X");
	}

	@Test
	public void shouldCallFilterWhenCallableReturnsValue() {
		Function<String, Object> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn("Y");
		when(filter.test("X")).thenReturn(true);
		Try.of(() -> "X").filter(filter).map(function).get();
		verify(filter, times(1)).test("X");
	}
}
