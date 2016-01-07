package com.nextbreakpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TryTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void empty_shouldReturnSuccess() {
		Try<Object, Exception> t = Try.empty(e -> e);
		assertFalse(t.isFailure());
		assertTrue(t.isSuccess());
	}
	
	@Test
	public void empty_shouldReturnSuccessWithoutValue() {
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
	public void ifPresent_givenTryIsEmpty_shouldNotHaveValue() throws Exception {
		Try<Object, Exception> t = Try.empty(e -> e);
		assertFalse(t.isPresent());
	}

	@Test
	public void ifPresentConsumer_givenTryIsEmpty_shouldNotCallConsumer() throws Exception {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Exception> t = Try.empty(e -> e);
		t.ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrow_givenIsEmpty_shouldNotCallConsumer() throws Exception {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Exception> t = Try.empty(e -> e);
		t.ifPresentOrThrow(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwException_givenIsEmpty_shouldDoNothing() throws Exception {
		Try<Object, Exception> t = Try.empty(e -> e);
		t.throwException();
	}

	@Test
	public void value_givenIsEmpty_shouldReturnEmptyOptional() throws Exception {
		Try<Object, Exception> t = Try.empty(e -> e);
		Optional<Object> value = t.value();
		assertFalse(value.isPresent());
	}

	@Test
	public void success_shouldReturnSuccess() {
		Try<Object, Exception> t = Try.success(e -> e, "X");
		assertFalse(t.isFailure());
		assertTrue(t.isSuccess());
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
	public void ifPresent_givenTryIsSuccessWithNullValue_shouldNotHaveValue() throws Exception {
		Try<Object, Exception> t = Try.success(e -> e, null);
		assertFalse(t.isPresent());
	}

	@Test
	public void ifPresentConsumer_givenTryIsSuccessWithNullValue_shouldNotCallConsumer() throws Exception {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Exception> t = Try.success(e -> e, null);
		t.ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrow_givenIsSuccessWithNullValue_shouldNotCallConsumer() throws Exception {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Exception> t = Try.success(e -> e, null);
		t.ifPresentOrThrow(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwException_givenIsSuccessWithNullValue_shouldDoNothing() throws Exception {
		Try<Object, Exception> t = Try.success(e -> e, null);
		t.throwException();
	}

	@Test
	public void value_givenIsSuccessWithNullValue_shouldReturnEmptyOptional() throws Exception {
		Try<Object, Exception> t = Try.success(e -> e, null);
		Optional<Object> value = t.value();
		assertFalse(value.isPresent());
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
	public void ifPresent_givenTryIsSuccessWithValue_shouldHaveValue() throws Exception {
		Try<Object, Exception> t = Try.success(e -> e, "X");
		assertTrue(t.isPresent());
	}

	@Test
	public void ifPresentConsumer_givenTryIsSuccessWithValue_shouldCallConsumer() throws Exception {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Exception> t = Try.success(e -> e, "X");
		t.ifPresent(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifPresentOrThrow_givenIsSuccessWithValue_shouldCallConsumer() throws Exception {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Exception> t = Try.success(e -> e, "X");
		t.ifPresentOrThrow(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void throwException_givenIsSuccessWithValue_shouldDoNothing() throws Exception {
		Try<Object, Exception> t = Try.success(e -> e, "X");
		t.throwException();
	}

	@Test
	public void value_givenIsSuccessWithValue_shouldReturnNotEmptyOptional() throws Exception {
		Try<Object, Exception> t = Try.success(e -> e, "X");
		Optional<Object> value = t.value();
		assertTrue(value.isPresent());
	}

	@Test
	public void failure_shouldReturnFailure() {
		Try<Object, Exception> t = Try.failure(e -> e, new Exception());
		assertFalse(t.isSuccess());
		assertTrue(t.isFailure());
	}

	@Test
	public void failure_shouldThrowExceptionWhenNullException() {
		exception.expect(NullPointerException.class);
		Try.failure(e -> e, null);
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

	@Test
	public void ifPresent_givenTryIsFailure_shouldNotHaveValue() throws Exception {
		Try<Object, Exception> t = Try.failure(e -> e, new Exception());
		assertFalse(t.isPresent());
	}

	@Test
	public void ifPresentConsumer_givenTryIsFailure_shouldNotCallConsumer() throws Exception {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Exception> t = Try.failure(e -> e, new Exception());
		t.ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrow_givenTryIsFailure_shouldThrowException() throws Exception {
		exception.expect(Exception.class);
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Exception> t = Try.failure(e -> e, new Exception());
		t.ifPresentOrThrow(consumer);
	}

	@Test
	public void throwException_givenIsFailure_shouldThrowException() throws Exception {
		exception.expect(Exception.class);
		Try<Object, Exception> t = Try.failure(e -> e, new Exception());
		t.throwException();
	}

	@Test
	public void value_givenIsFailure_shouldReturnEmptyOptional() throws Exception {
		Try<Object, Exception> t = Try.failure(e -> e, new Exception());
		Optional<Object> value = t.value();
		assertFalse(value.isPresent());
	}

	@Test
	public void ofNullable_givenNullValue_shouldReturnSuccess() {
		Try<Object, Exception> t = Try.ofNullable(e -> e, null);
		assertTrue(t.isSuccess());
	}

	@Test
	public void of_givenNullValue_shouldReturnThrowException() {
		exception.expect(Exception.class);
		Try.of(e -> e, null);
	}

	@Test
	public void ofNullable_givenNotNullValue_shouldReturnSuccess() {
		Try<Object, Exception> t = Try.ofNullable(e -> e, "X");
		assertTrue(t.isSuccess());
	}

	@Test
	public void of_givenNotNullValue_shouldReturnSuccess() {
		Try<Object, Exception> t = Try.of(e -> e, "X");
		assertTrue(t.isSuccess());
	}

	@Test
	public void of_givenNullBlock_shouldThrowException() {
		exception.expect(Exception.class);
		Try.of(e -> e, null);
	}

	@Test
	public void of_givenBlockReturnsObject_shouldReturnSuccess() {
		Try<Object, Exception> t = Try.of(e -> e, () -> new Object());
		assertTrue(t.isSuccess());
	}

	@Test
	public void of_givenBlockThrowsException_shouldReturnFailure() {
		Try<Object, Exception> t = Try.of(e -> e, () -> { throw new Exception(); });
		assertTrue(t.isFailure());
	}

	@Test
	public void of_givenBlockReturnsObject_shouldReturnSameObject() {
		Object value = new Object();
		Try<Object, Exception> t = Try.of(e -> e, () -> value);
		assertEquals(value, t.get());
	}

	@Test
	public void orElseTry_givenBlockReturnsValue_shouldNotCallElseBlock() {
		Object value = new Object();
		Object elseValue = new Object();
		Object result = Try.of(e -> e, () -> value).orElseTry(() -> elseValue).get();
		assertEquals(value, result);
	}

	@Test
	public void orElseTry_givenBlockThrowsException_shouldCallElseBlockAndReturnValue() {
		Object value = new Object();
		Object result = Try.of(e -> e, () -> { throw new Exception(); }).orElseTry(() -> value).get();
		assertEquals(value, result);
	}
}
