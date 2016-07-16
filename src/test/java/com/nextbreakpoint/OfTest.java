package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotNull;

public class OfTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowNullPointerExceptionWhenCallableIsNull() {
		exception.expect(NullPointerException.class);
		Try.of(null);
	}

	@Test
	public void shouldNotReturnNullWhenCallableIsNotNullAndReturnsValue() {
		assertNotNull(Try.of(() -> "X"));
	}

	@Test
	public void shouldNotReturnNullWhenCallableIsNotNullAndReturnsNull() {
		assertNotNull(Try.of(() -> null));
	}

	@Test
	public void shouldNotReturnNullWhenCallableIsNotNullAndThrowsException() {
		assertNotNull(Try.of(() -> { throw new Exception(); }));
	}
}
