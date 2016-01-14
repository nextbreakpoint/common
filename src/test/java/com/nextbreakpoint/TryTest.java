package com.nextbreakpoint;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TryTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void success_shouldReturnSuccess() {
		Try<Object, Throwable> t = Try.success("X");
		assertFalse(t.isFailure());
	}
	
	@Test
	public void get_givenTryIsSuccessWithNullValue_shouldThrowException() {
		exception.expect(NoSuchElementException.class);
		Try.success(null).get();
	}
	
	@Test
	public void getOrElse_givenTryIsSuccessWithNullValue_shouldReturnElseValue() {
		assertEquals("X", Try.success(null).getOrElse("X"));
	}
	
	@Test
	public void getOrThrow_givenTryIsSuccessWithNullValue_shouldThrowException() throws Throwable {
		exception.expect(NoSuchElementException.class);
		Try.success(null).getOrThrow();
	}

	@Test
	public void isPresent_givenTryIsSuccessWithNullValue_shouldNotHaveValue() throws Throwable {
		Try<Object, Throwable> t = Try.success(null);
		assertFalse(t.isPresent());
	}

	@Test
	public void ifPresentConsumer_givenTryIsSuccessWithNullValue_shouldNotCallConsumer() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Throwable> t = Try.success(null);
		t.ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrow_givenIsSuccessWithNullValue_shouldNotCallConsumer() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Throwable> t = Try.success(null);
		t.ifPresentOrThrow(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwException_givenIsSuccessWithNullValue_shouldDoNothing() throws Throwable {
		Try<Object, Throwable> t = Try.success(null);
		t.throwException();
	}

	@Test
	public void value_givenIsSuccessWithNullValue_shouldReturnEmptyOptional() throws Throwable {
		Try<Object, Throwable> t = Try.success(null);
		Optional<Object> value = t.value();
		assertFalse(value.isPresent());
	}

	@Test
	public void map_givenTryIsSuccessWithNullValue_shouldNotCallFunction() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<Object, Object> function = mock(Function.class);
		Try<Object, Throwable> t = Try.success(null);
		t.map(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void flatMap_givenTryIsSuccessWithNullValue_shouldNotCallFunction() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<Object, Try<Object, Throwable>> function = mock(Function.class);
		Try<Object, Throwable> t = Try.success(null);
		t.flatMap(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void get_givenTryIsSuccessWithValue_shouldReturnSameValue() {
		Try<Object, Throwable> t = Try.success("X");
		assertEquals("X", t.get());
	}

	@Test
	public void getOrElse_givenTryIsSuccessWithValue_shouldReturnSameValue() {
		Try<Object, Throwable> t = Try.success("X");
		assertEquals("X", t.getOrElse("Y"));
	}

	@Test
	public void getOrThrow_givenTryIsSuccessWithValue_shouldReturnSameValue() throws Throwable {
		Try<Object, Throwable> t = Try.success("X");
		assertEquals("X", t.getOrThrow());
	}

	@Test
	public void isPresent_givenTryIsSuccessWithValue_shouldHaveValue() throws Throwable {
		Try<Object, Throwable> t = Try.success("X");
		assertTrue(t.isPresent());
	}

	@Test
	public void ifPresentConsumer_givenTryIsSuccessWithValue_shouldCallConsumer() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Throwable> t = Try.success("X");
		t.ifPresent(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifPresentOrThrow_givenIsSuccessWithValue_shouldCallConsumer() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Throwable> t = Try.success("X");
		t.ifPresentOrThrow(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void throwThrowable_givenIsSuccessWithValue_shouldDoNothing() throws Throwable {
		Try<Object, Throwable> t = Try.success("X");
		t.throwException();
	}

	@Test
	public void value_givenIsSuccessWithValue_shouldReturnNotEmptyOptional() throws Throwable {
		Try<Object, Throwable> t = Try.success("X");
		Optional<Object> value = t.value();
		assertTrue(value.isPresent());
	}

	@Test
	public void map_givenTryIsSuccessWithValue_shouldCallFunction() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<Object, Object> function = mock(Function.class);
		Try<Object, Throwable> t = Try.success("X");
		t.map(function);
		verify(function, times(1)).apply("X");
	}

	@Test
	public void flatMap_givenTryIsSuccessWithValue_shouldCallFunction() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<Object, Try<Object, Throwable>> function = mock(Function.class);
		Try<Object, Throwable> t = Try.success("X");
		t.flatMap(function);
		verify(function, times(1)).apply("X");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void map_givenTryIsSuccessWithValueAndFunctionThrowsRuntimeException_shouldReturnFailure() throws Throwable {
		Function<Object, Object> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		Try<Object, Throwable> t = Try.success("X");
		Try<Object, Throwable> q = t.map(function);
		assertTrue(q.isFailure());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void flatMap_givenTryIsSuccessWithValueAndFunctionThrowsRuntimeException_shouldReturnFailure() throws Throwable {
		Function<Object, Try<Object, Throwable>> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		Try<Object, Throwable> t = Try.success("X");
		Try<Object, Throwable> q = t.flatMap(function);
		assertTrue(q.isFailure());
	}
	
	@Test
	public void failure_shouldReturnFailure() {
		Try<Object, Throwable> t = Try.failure(new Throwable());
		assertTrue(t.isFailure());
	}

	@Test
	public void failure_shouldThrowThrowableWhenNullThrowable() {
		exception.expect(NullPointerException.class);
		Try.failure(null);
	}

	@Test
	public void get_givenTryIsFailure_shouldThrowThrowable() {
		exception.expect(NoSuchElementException.class);
		Try<Object, Throwable> t = Try.failure(new Throwable());
		t.get();
	}

	@Test
	public void getOrElse_givenTryIsFailure_shouldReturnElseValue() {
		Try<Object, Throwable> t = Try.failure(new Throwable());
		assertEquals("X", t.getOrElse("X"));
	}

	@Test
	public void getOrThrow_givenTryIsFailure_shouldThrowThrowable() throws Throwable {
		exception.expect(IllegalAccessException.class);
		Try<Object, Throwable> t = Try.failure(new IllegalAccessException());
		t.getOrThrow();
	}
 
	@Test
	public void isPresent_givenTryIsFailure_shouldNotHaveValue() throws Throwable {
		Try<Object, Throwable> t = Try.failure(new Exception());
		assertFalse(t.isPresent());
	}

	@Test
	public void ifPresentConsumer_givenTryIsFailure_shouldNotCallConsumer() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Throwable> t = Try.failure(new Exception());
		t.ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrow_givenTryIsFailure_shouldThrowException() throws Throwable {
		exception.expect(Exception.class);
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try<Object, Throwable> t = Try.failure(new Exception());
		t.ifPresentOrThrow(consumer);
	}

	@Test
	public void throwException_givenIsFailure_shouldThrowException() throws Throwable {
		exception.expect(Exception.class);
		Try<Object, Throwable> t = Try.failure(new Exception());
		t.throwException();
	}

	@Test
	public void value_givenIsFailure_shouldReturnEmptyOptional() throws Throwable {
		Try<Object, Throwable> t = Try.failure(new Exception());
		Optional<Object> value = t.value();
		assertFalse(value.isPresent());
	}

	@Test
	public void map_givenTryIsFailure_shouldCallFunction() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<Object, Object> function = mock(Function.class);
		Try<Object, Throwable> t = Try.failure(new Exception());
		t.map(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void flatMap_givenTryIsFailure_shouldCallFunction() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<Object, Try<Object, Throwable>> function = mock(Function.class);
		Try<Object, Throwable> t = Try.failure(new Exception());
		t.flatMap(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void of_givenNullSupplier_shouldThrowException() {
		exception.expect(Exception.class);
		Try.of(null);
	}

	@Test
	public void of_givenSupplierReturnsObject_shouldReturnSuccess() {
		Try<Object, Throwable> t = Try.of(() -> new Object());
		assertFalse(t.isFailure());
	}

	@Test
	public void of_givenSupplierThrowsException_shouldReturnFailure() {
		Try<Object, Throwable> t = Try.of(() -> { throw new Exception(); });
		assertTrue(t.isFailure());
	}

	@Test
	public void of_givenSupplierReturnsObject_shouldReturnSameObject() {
		Try<Object, Throwable> t = Try.of(() -> "X");
		assertEquals("X", t.get());
	}

	@Test
	public void or_givenFirstSupplierReturnsValue_shouldNotCallSecondSupplier() {
		Object result = Try.of(() -> "X").or(() -> "Y").get();
		assertEquals("X", result);
	}

	@Test
	public void or_givenFirstSupplierThrowsException_shouldCallSecondSupplierAndReturnValue() {
		Object result = Try.of(() -> { throw new Exception(); }).or(() -> "Y").get();
		assertEquals("Y", result);
	}

	@Test
	public void or_givenFirstSupplierAndSecondSupplierThrowException_shouldReturnFailure() {
		Try<Object, Throwable> q = Try.of(() -> { throw new Exception(); }).or(() -> { throw new Exception(); });
		assertTrue(q.isFailure());
	}

	@Test
	public void of_givenSupplierThrowsIOException_shouldReturnFailure() {
		Function<Throwable, IOException> mapper = e -> (e instanceof IOException) ? (IOException)e : new IOException(e);
		Try<Object, IOException> t = Try.of(mapper, () -> { throw new IOException(); });
		assertTrue(t.isFailure());
	}

	@Test
	public void throwException_givenSupplierThrowsIOExceptionndMapperReturnsIOException_shouldThrowSameException() throws IOException {
		IOException x = new IOException();
		exception.expect(is(x));
		Function<Throwable, IOException> mapper = e -> (e instanceof IOException) ? (IOException)e : new IOException(e);
		Try<Object, IOException> t = Try.of(mapper, () -> { throw x; });
		t.throwException();
	}

	@Test
	public void throwException_givenSupplierThrowsNullPointerExceptionAndMapperReturnsIOException_shouldThrowIOExceptionWithCause() throws IOException {
		NullPointerException x = new NullPointerException();
		exception.expectCause(is(x));
		Function<Throwable, IOException> mapper = e -> (e instanceof IOException) ? (IOException)e : new IOException(e);
		Try<Object, IOException> t = Try.of(mapper, () -> { throw x; });
		t.throwException();
	}

	@Test
	public void of_givenMapperReturnsIOExceptionAndSupplierReturnsObject_shouldReturnSuccess() {
		Function<Throwable, IOException> mapper = e -> (e instanceof IOException) ? (IOException)e : new IOException(e);
		Try<Object, IOException> t = Try.of(mapper, () -> new Object());
		assertFalse(t.isFailure());
	}
}
