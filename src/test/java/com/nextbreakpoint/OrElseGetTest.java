package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OrElseGetTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldReturnSupplierValueWhenFailure() {
		assertEquals("X", Try.failure(new Exception()).orElseGet(() -> "X"));
	}

	@Test
	public void shouldReturnNullWhenFailureAndSupplierReturnsNull() {
		assertNull(Try.failure(new Exception()).orElseGet(() -> null));
	}

	@Test
	public void shouldReturnSupplierValueWhenSuccessAndValueIsNull() {
		assertEquals("X", Try.success(null).orElseGet(() -> "X"));
	}

	@Test
	public void shouldReturnNullWhenSuccessAndValueIsNullAndSupplierReturnsNull() {
		assertNull(Try.success(null).orElseGet(() -> null));
	}

	@Test
	public void shouldReturnValueWhenSuccessAndValueIsNotNull() {
		assertEquals("X", Try.success("X").orElseGet(() -> "X"));
	}

	@Test
	public void shouldReturnValueWhenSuccessAndValueIsNotNullAndSupplierReturnsNull() {
		assertEquals("X", Try.success("X").orElseGet(() -> null));
	}

	@Test
	public void shouldReturnSupplierValueWhenCallableThrowsException() {
		assertEquals("X", Try.of(() -> { throw new Exception(); }).orElseGet(() -> "X"));
	}

	@Test
	public void shouldReturnNullWhenCallableThrowsExceptionAndSupplierReturnsNull() {
		assertNull(Try.of(() -> { throw new Exception(); }).orElseGet(() -> null));
	}

	@Test
	public void shouldReturnSupplierValueWhenCallableReturnsNull() {
		assertEquals("X", Try.of(() -> null).orElseGet(() -> "X"));
	}

	@Test
	public void shouldReturnNullWhenCallableReturnsNullAndSupplierReturnsNull() {
		assertNull(Try.of(() -> null).orElseGet(() -> null));
	}

	@Test
	public void shouldReturnValueWhenCallableReturnsValue() {
		assertEquals("X", Try.of(() -> "X").orElseGet(() -> "Y"));
	}

	@Test
	public void shouldReturnValueWhenCallableReturnsValueAndSupplierReturnsNull() {
		assertEquals("X", Try.of(() -> "X").orElseGet(() -> null));
	}
}
