package com.nextbreakpoint;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsFailureTest {
	@Test
	public void shouldReturnTrueWhenFailure() {
		assertTrue(Try.failure(new Exception()).isFailure());
	}

	@Test
	public void shouldReturnFalseWhenSuccessAndValueIsNull() {
		assertFalse(Try.success(null).isFailure());
	}

	@Test
	public void shouldReturnFalseWhenSuccessAndValueIsNotNull() {
		assertFalse(Try.success("X").isFailure());
	}

	@Test
	public void shouldReturnTrueWhenCallableThrowsException() {
		assertTrue(Try.of(() -> { throw new Exception(); }).isFailure());
	}

	@Test
	public void shouldReturnFalseWhenCallableReturnsNull() {
		assertFalse(Try.of(() -> null).isFailure());
	}

	@Test
	public void shouldReturnFalseWhenCallableReturnsValue() {
		assertFalse(Try.of(() -> "X").isFailure());
	}
}
