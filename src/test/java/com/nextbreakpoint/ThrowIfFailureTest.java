package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ThrowIfFailureTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowExceptionWhenFailure() throws Exception {
		exception.expect(Exception.class);
		Try.failure(new Exception()).throwIfFailure();
	}

	@Test
	public void shouldNotThrowExceptionWhenSuccessAndValueIsNull() throws Exception {
		Try.success(null).throwIfFailure();
	}

	@Test
	public void shouldNotThrowExceptionWhenSuccessAndValueIsNotNull() throws Exception {
		Try.success("X").throwIfFailure();
	}

	@Test
	public void shouldThrowExceptionWhenCallableThrowsException() throws Exception {
		exception.expect(Exception.class);
		Try.of(() -> { throw new Exception(); }).throwIfFailure();
	}

	@Test
	public void shouldNotThrowExceptionWhenCallableReturnsNull() throws Exception {
		Try.of(() -> null).throwIfFailure();
	}

	@Test
	public void shouldNotThrowExceptionWhenCallableReturnsValue() throws Exception {
		Try.of(() -> "X").throwIfFailure();
	}
}
