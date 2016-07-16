package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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
	public void shouldNotBeNullWhenExceptionIsNotNull() {
		assertNotNull(Try.failure(new Exception()));
	}

	@Test
	public void shouldNotBeNullWhenExceptionIsNotNullAndMapperIsNotNull() {
		assertNotNull(Try.failure(new Exception()).mapper(e -> e));
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenMapperIsNull() {
		exception.expect(NullPointerException.class);
		Try.failure(new Exception()).mapper(null);
	}

	@Test
	public void isFailureShouldReturnTrue() {
		assertTrue(Try.failure(new Exception()).isFailure());
	}

	@Test
	public void isSuccessShouldReturnFalse() {
		assertFalse(Try.failure(new Exception()).isSuccess());
	}

	@Test
	public void getShouldThrowNoSuchElementException() {
		exception.expect(NoSuchElementException.class);
		Try.failure(new Exception()).get();
	}

	@Test
	public void orElseShouldReturnDefaultValue() {
		assertEquals("X", Try.failure(new Exception()).orElse("X"));
	}

	@Test
	public void orElseShouldReturnSupplierValue() {
		assertEquals("X", Try.failure(new Exception()).orElseGet(() -> "X"));
	}

	@Test
	public void orElseShouldReturnNullWhenSupplierReturnsNull() {
		assertNull(Try.failure(new Exception()).orElseGet(() -> null));
	}

	@Test
	public void orThrowShouldThrowException() throws Exception {
		exception.expect(IllegalAccessException.class);
		Try.failure(new IllegalAccessException()).orThrow();
	}

	@Test
	public void orThrowWithDefaultValueShouldThrowException() throws Exception {
		exception.expect(IllegalAccessException.class);
		Try.failure(new IllegalAccessException()).orThrow("X");
	}

	@Test
	public void isPresentShouldReturnFalse() throws Exception {
		assertFalse(Try.failure(new Exception()).isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldNotCallConsumer() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldThrowException() throws Exception {
		exception.expect(Exception.class);
		Consumer<Object> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifPresentOrThrow(consumer);
	}

	@Test
	public void ifFailureShouldCallConsumer() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifFailure(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifSuccessShouldNotCallConsumer() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.failure(new Exception()).ifSuccess(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwExceptionShouldThrowException() throws Exception {
		exception.expect(Exception.class);
		Try.failure(new Exception()).throwIfFailure();
	}

	@Test
	public void valueShouldReturnEmptyOptional() {
		assertFalse(Try.failure(new Exception()).value().isPresent());
	}

	@Test
	public void shouldNotCallFunctionForMap() {
		Function<Object, Object> function = mock(Function.class);
		Try.failure(new Exception()).map(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldNotCallFunctionForFlatMap() {
		Function<Object, Try<Object, Exception>> function = mock(Function.class);
		Try.failure(new Exception()).flatMap(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldNotCallSuccessConsumer() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.failure(new Exception()).onSuccess(consumer).orElse(null);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void shouldCallFailureConsumer() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.failure(new Exception()).onFailure(consumer).orElse(null);
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldThrowsIOException() throws IOException {
		exception.expect(IOException.class);
		Try.failure(new Exception()).mapper(testMapper()).throwIfFailure();
	}

	@Test
	public void shouldCallSecondCallableForOr() throws Exception {
		Callable<Object> callable = mock(Callable.class);
		when(callable.call()).thenReturn("X");
		Try.failure(new Exception()).or(callable).isFailure();
		verify(callable, times(1)).call();
	}

	@Test
	public void shouldNotCallSecondCallableForAnd() throws Exception {
		Callable<Object> callable = mock(Callable.class);
		when(callable.call()).thenReturn("X");
		Try.failure(new Exception()).and(callable).isFailure();
		verify(callable, times(0)).call();
	}

	@Test
	public void shouldNotCallFilterForMap() {
		Function<Object, Object> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn("Y");
		when(filter.test("X")).thenReturn(true);
		Try.failure(new Exception()).filter(filter).map(function).isFailure();
		verify(filter, times(0)).test("X");
	}

	@Test
	public void shouldNotCallFilterForFlatMap() {
		Function<Object, Try<Object, Exception>> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn(Try.success("Y"));
		when(filter.test("X")).thenReturn(true);
		Try.failure(new Exception()).filter(filter).flatMap(function).isFailure();
		verify(filter, times(0)).test("X");
	}

	private static Function<Exception, IOException> testMapper() {
		return e -> (e instanceof IOException) ? (IOException)e : new IOException("IO Error", e);
	}
}
