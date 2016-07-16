package com.nextbreakpoint;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrElseTest {
	@Test
	public void shouldReturnDefaultValueWhenFailure() {
		assertEquals("X", Try.failure(new Exception()).orElse("X"));
	}

	@Test
	public void shouldReturnDefaultWhenSuccessAndValueIsNull() {
		assertEquals("X", Try.success(null).orElse("X"));
	}

	@Test
	public void shouldReturnValueWhenSuccessAndValueIsNotNull() {
		assertEquals("X", Try.success("X").orElse("Y"));
	}

	@Test
	public void shouldReturnDefaultValueWhenCallableThrowsException() {
		assertEquals("X", Try.of(() -> { throw new Exception(); }).orElse("X"));
	}

	@Test
	public void shouldReturnDefaultValueWhenCallableReturnsNull() {
		assertEquals("X", Try.of(() -> null).orElse("X"));
	}

	@Test
	public void shouldReturnValueWhenCallableReturnsValue() {
		assertEquals("X", Try.of(() -> "X").orElse("Y"));
	}
}
