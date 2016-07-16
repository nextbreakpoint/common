package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsFailureTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

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
