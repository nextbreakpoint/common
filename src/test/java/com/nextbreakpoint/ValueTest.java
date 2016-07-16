package com.nextbreakpoint;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValueTest {
	@Test
	public void shouldReturnEmptyOptionalWhenFailure() {
		assertFalse(Try.failure(new Exception()).value().isPresent());
	}

	@Test
	public void shouldReturnEmptyOptionalWhenSuccessAndValueIsNull() {
		assertFalse(Try.success(null).value().isPresent());
	}

	@Test
	public void shouldReturnNotEmptyOptionalWhenSuccessAndValueIsNotNull() {
		assertTrue(Try.success("X").value().isPresent());
	}

	@Test
	public void shouldReturnEmptyOptionalWhenCallableThrowsException() {
		assertFalse(Try.of(() -> { throw new Exception(); }).value().isPresent());
	}

	@Test
	public void shouldReturnEmptyOptionalWhenCallableReturnsNull()  {
		assertFalse(Try.of(() -> null).value().isPresent());
	}

	@Test
	public void shouldReturnNotEmptyOptionalWhenCallableReturnsValue() {
		assertTrue(Try.of(() -> "X").value().isPresent());
	}
}
