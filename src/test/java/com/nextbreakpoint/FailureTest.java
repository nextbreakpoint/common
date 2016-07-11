package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class FailureTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowNullPointerExceptionWhenExceptionIsNull() {
		exception.expect(NullPointerException.class);
		Try.failure(null);
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenExceptionIsNullAndMapperIsNotNull() {
		exception.expect(NullPointerException.class);
		Try.failure(e -> e, null);
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenMapperIsNull() {
		exception.expect(NullPointerException.class);
		Try.failure(null, new Exception());
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenExceptionIsNullAndMapperIsNull() {
		exception.expect(NullPointerException.class);
		Try.failure(null, null);
	}

	@Test
	public void shouldNotReturnNullWhenExceptionIsNotNull() {
		assertNotNull(Try.failure(new Exception()));
	}

	@Test
	public void shouldNotReturnNullWhenExceptionIsNotNullAndMapperIsNotNull() {
		assertNotNull(Try.failure(e -> e, new Exception()));
	}

	@Test
	public void isFailureShouldReturnTrue() {
		assertTrue(Try.failure(new Throwable()).isFailure());
	}

	@Test
	public void getShouldThrowNoSuchElementException() {
		exception.expect(NoSuchElementException.class);
		Try.failure(new Throwable()).get();
	}

	@Test
	public void getOrElseShouldReturnElseValue() {
		assertEquals("X", Try.failure(new Throwable()).getOrElse("X"));
	}

	@Test
	public void getOrThrowShouldThrowException() throws Throwable {
		exception.expect(IllegalAccessException.class);
		Try.failure(new IllegalAccessException()).getOrThrow();
	}

	@Test
	public void getOrThrowWithValueShouldThrowException() throws Throwable {
		exception.expect(IllegalAccessException.class);
		Try.failure(new IllegalAccessException()).getOrThrow("X");
	}

	@Test
	public void isPresentShouldReturnFalse() throws Throwable {
		assertFalse(Try.failure(new Exception()).isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldNotCallConsumer() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldThrowException() throws Throwable {
		exception.expect(Exception.class);
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifPresentOrThrow(consumer);
	}

	@Test
	public void ifFailureShouldCallConsumer() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifFailure(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void throwExceptionShouldThrowException() throws Throwable {
		exception.expect(Exception.class);
		Try.failure(new Exception()).throwException();
	}

	@Test
	public void valueShouldReturnEmptyOptional() {
		assertFalse(Try.failure(new Exception()).value().isPresent());
	}

	@Test
	public void mapShouldNotCallFunction() {
		@SuppressWarnings("unchecked")
		Function<Object, Object> function = mock(Function.class);
		Try.failure(new Exception()).map(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void flatMapShouldNotCallFunction() {
		@SuppressWarnings("unchecked")
		Function<Object, Try<Object, Throwable>> function = mock(Function.class);
		Try.failure(new Exception()).flatMap(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void onSuccessShouldNotCallConsumer() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.failure(new Exception()).onSuccess(consumer);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void getShouldCallOnFailureHandler() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.failure(new Exception()).onFailure(consumer).getOrElse(null);
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void convertShouldReturnFailure() {
		assertTrue(Try.failure(new Exception()).convert(testMapper()).isFailure());
	}

	@Test
	public void convertShouldReturnFailureWhichThrowsIOException() throws IOException {
		exception.expect(IOException.class);
		Try.failure(new Exception()).convert(testMapper()).throwException();
	}

	private static Function<Throwable, IOException> testMapper() {
		return e -> (e instanceof IOException) ? (IOException)e : new IOException("IO Error", e);
	}
}
