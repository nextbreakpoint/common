package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class GetTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowNoSuchElementExceptionWhenFailure() {
		exception.expect(NoSuchElementException.class);
		Try.failure(new Exception()).get();
	}

	@Test
	public void shouldThrowNoSuchElementExceptionWhenSuccessAndValueIsNull() {
		exception.expect(NoSuchElementException.class);
		Try.success(null).get();
	}

	@Test
	public void shouldReturnValueWhenSuccessAndValueIsNotNull() {
		assertEquals("X", Try.success("X").get());
	}

	@Test
	public void shouldThrowNoSuchElementExceptionWhenCallableThrowsException() {
		exception.expect(NoSuchElementException.class);
		Try.of(() -> { throw new Exception(); }).get();
	}

	@Test
	public void shouldThrowNoSuchElementExceptionWhenCallableReturnsNull() {
		exception.expect(NoSuchElementException.class);
		Try.of(() -> null).get();
	}

	@Test
	public void shouldReturnValueWhenCallableReturnsValue() {
		assertEquals("X", Try.of(() -> "X").get());
	}
}
