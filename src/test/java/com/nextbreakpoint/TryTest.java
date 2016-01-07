package com.nextbreakpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TryTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void empty_shouldReturnSuccess() {
		Try<Object, Exception> t = Try.empty(e -> e);
		assertTrue(t.isSuccess());
	}
	
	@Test
	public void empty_shouldReturnSuccessWithNullValue() {
		Try<Object, Exception> t = Try.empty(e -> e);
		assertFalse(t.isPresent());
	}
	
	@Test
	public void get_givenTryIsEmpty_shouldThrowException() {
		exception.expect(NoSuchElementException.class);
		Try.empty(e -> e).get();
	}
	
	@Test
	public void getOrElse_givenTryIsEmpty_shouldReturnElseValue() {
		assertEquals("X", Try.empty(e -> e).getOrElse("X"));
	}
	
	@Test
	public void getOrThrow_givenTryIsEmpty_shouldThrowException() throws Exception {
		exception.expect(NoSuchElementException.class);
		Try.empty(e -> e).getOrThrow();
	}
	
	@Test
	public void success_shouldReturnSuccess() {
		Try<Object, Exception> t = Try.success(e -> e, "X");
		assertTrue(t.isSuccess());
		assertTrue(t.isPresent());
	}
	
	@Test
	public void get_givenTryIsSuccessWithNullValue_shouldThrowException() {
		exception.expect(NoSuchElementException.class);
		Try.success(e -> e, null).get();
	}
	
	@Test
	public void getOrElse_givenTryIsSuccessWithNullValue_shouldReturnElseValue() {
		assertEquals("X", Try.success(e -> e, null).getOrElse("X"));
	}
	
	@Test
	public void getOrThrow_givenTryIsSuccessWithNullValue_shouldThrowException() throws Exception {
		exception.expect(NoSuchElementException.class);
		Try.success(e -> e, null).getOrThrow();
	}

	@Test
	public void get_givenTryIsSuccessWithValue_shouldReturnSameValue() {
		Try<Object, Exception> t = Try.success(e -> e, "X");
		assertEquals("X", t.get());
	}

	@Test
	public void getOrElse_givenTryIsSuccessWithValue_shouldReturnSameValue() {
		Try<Object, Exception> t = Try.success(e -> e, "X");
		assertEquals("X", t.getOrElse("Y"));
	}

	@Test
	public void getOrThrow_givenTryIsSuccessWithValue_shouldReturnSameValue() throws Exception {
		Try<Object, Exception> t = Try.success(e -> e, "X");
		assertEquals("X", t.getOrThrow());
	}

	@Test
	public void get_givenTryIsFailure_shouldThrowException() {
		exception.expect(NoSuchElementException.class);
		Try<Object, Exception> t = Try.failure(e -> e, new Exception());
		assertEquals("X", t.get());
	}

	@Test
	public void getOrElse_givenTryIsFailure_shouldReturnElseValue() {
		Try<Object, Exception> t = Try.failure(e -> e, new Exception());
		assertEquals("X", t.getOrElse("X"));
	}

	@Test
	public void getOrThrow_givenTryIsFailure_shouldThrowException() throws Exception {
		exception.expect(IllegalAccessException.class);
		Try<Object, Exception> t = Try.failure(e -> e, new IllegalAccessException());
		assertEquals("X", t.getOrThrow());
	}
}
