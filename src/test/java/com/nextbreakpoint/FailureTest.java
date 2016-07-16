package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotNull;

public class FailureTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowNullPointerExceptionWhenExceptionIsNull() {
		exception.expect(NullPointerException.class);
		Try.failure(null);
	}

	@Test
	public void shouldNotReturnNullWhenExceptionIsNotNull() {
		assertNotNull(Try.failure(new Exception()));
	}
}
