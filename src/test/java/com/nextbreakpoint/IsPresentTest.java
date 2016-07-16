package com.nextbreakpoint;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsPresentTest {
	@Test
	public void shouldReturnFalseWhenFailure() throws Exception {
		assertFalse(Try.failure(new Exception()).isPresent());
	}

	@Test
	public void shouldReturnFalseWhenSuccessAndValueIsNull() {
		assertFalse(Try.success(null).isPresent());
	}

	@Test
	public void shouldReturnTrueWhenSuccessAndValueIsNotNull() {
		assertTrue(Try.success("X").isPresent());
	}

	@Test
	public void shouldReturnFalseWhenCallableThrowsException() {
		assertFalse(Try.of(() -> { throw new Exception(); }).isPresent());
	}

	@Test
	public void shouldReturnFalseWhenCallableReturnsNull() {
		assertFalse(Try.of(() -> null).isPresent());
	}

	@Test
	public void shouldReturnTrueWhenCallableReturnsValue() {
		assertTrue(Try.of(() -> "X").isPresent());
	}
}
