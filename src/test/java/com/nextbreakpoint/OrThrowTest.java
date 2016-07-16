package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class OrThrowTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowExceptionWhenFailure() throws Exception {
		exception.expect(IllegalAccessException.class);
		Try.failure(new IllegalAccessException()).orThrow();
	}

	@Test
	public void shouldThrowExceptionWhenFailureAndDefaultIsNotNull() throws Exception {
		exception.expect(IllegalAccessException.class);
		Try.failure(new IllegalAccessException()).orThrow("X");
	}

	@Test
	public void shouldThrowNoSuchElementExceptionWhenSuccessAndValueIsNull() throws Exception {
		exception.expect(NoSuchElementException.class);
		Try.success(null).orThrow();
	}

	@Test
	public void shouldReturnDefaultValueSuccessAndWhenValueIsNull() throws Exception {
		assertEquals("X", Try.success(null).orThrow("X"));
	}

	@Test
	public void shouldReturnValueWhenSuccessAndValueIsNotNull() throws Exception {
		assertEquals("X", Try.success("X").orThrow());
	}

	@Test
	public void shouldReturnValueWhenSuccessAndValueIsNotNullAndDefaultIsNotNull() throws Exception {
		assertEquals("X", Try.success("X").orThrow("Y"));
	}

	@Test
	public void shouldThrowNoSuchElementExceptionWhenCallableReturnsNull() throws Exception {
		exception.expect(NoSuchElementException.class);
		Try.of(() -> null).orThrow();
	}

	@Test
	public void shouldReturnDefaultValueWhenCallableReturnsNull() throws Exception {
		assertEquals("X", Try.of(() -> null).orThrow("X"));
	}

	@Test
	public void shouldReturnValueWhenCallableReturnsValue() throws Exception {
		assertEquals("X", Try.of(() -> "X").orThrow());
	}

	@Test
	public void shouldReturnValueWhenCallableReturnsValueAndDefaultIsNotNull() throws Exception {
		assertEquals("X", Try.of(() -> "X").orThrow("Y"));
	}

	@Test
	public void shouldThrowExceptionWhenCallableThrowsException() throws Exception {
		exception.expect(IllegalAccessException.class);
		Try.of(() -> { throw new IllegalAccessException(); }).orThrow();
	}

	@Test
	public void shouldThrowExceptionWhenCallableThrowsExceptionAndDefaultIsNotNull() throws Exception {
		exception.expect(IllegalAccessException.class);
		Try.of(() -> { throw new IllegalAccessException(); }).orThrow("X");
	}
}
