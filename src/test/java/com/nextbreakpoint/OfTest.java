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
	public void shouldNotReturnNullWhenCallableReturnsValue() {
		assertNotNull(Try.of(() -> "X"));
	}

	@Test
	public void shouldNotReturnNullWhenCallableReturnsNull() {
		assertNotNull(Try.of(() -> null));
	}

	@Test
	public void shouldNotReturnNullWhenCallableThrowsException() {
		assertNotNull(Try.of(() -> { throw new Exception(); }));
	}
}
