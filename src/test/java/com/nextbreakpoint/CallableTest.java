package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CallableTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowNullPointerExceptionWhenCallableIsNull() {
		exception.expect(NullPointerException.class);
		Try.of(null);
	}

	@Test
	public void shouldNotBeNullWhenCallableIsNotNull() {
		assertNotNull(Try.of(() -> "X"));
	}

	@Test
	public void shouldNotBeNullWhenCallableIsNotNullAndMapperIsNotNull() {
		assertNotNull(Try.of(() -> "X").withMapper(e -> e));
	}

	@Test
	public void shouldNotBeNullWhenCallableReturnNull() {
		assertNotNull(Try.of(() -> null));
	}

	@Test
	public void shouldNotBeNullWhenCallableReturnsNullAndMapperIsNotNull() {
		assertNotNull(Try.of(() -> null).withMapper(e -> e));
	}

	@Test
	public void shouldNotBeNullWhenCallableThrowsException() {
		assertNotNull(Try.of(() -> { throw new Exception(); }));
	}

	@Test
	public void shouldNotBeNullWhenCallableThrowsExceptionAndMapperIsNotNull() {
		assertNotNull(Try.of(() -> { throw new Exception(); }).withMapper(e -> e));
	}
	
	@Test
	public void isFailureShouldReturnFalseWhenCallableReturnsNull() {
		assertFalse(Try.of(() -> null).isFailure());
	}

	@Test
	public void getShouldThrowNoSuchElementExceptionWhenCallableReturnsNull() {
		exception.expect(NoSuchElementException.class);
		Try.of(() -> null).get();
	}

	@Test
	public void getOrElseShouldReturnDefaultValueWhenCallableReturnsNull() {
		assertEquals("X", Try.of(() -> null).getOrElse("X"));
	}

	@Test
	public void getOrThrowShouldThrowNoSuchElementExceptionWhenCallableReturnsNull() throws Throwable {
		exception.expect(NoSuchElementException.class);
		Try.of(() -> null).getOrThrow();
	}

	@Test
	public void getOrThrowWithDefaultValueShouldReturnDefaultValueWhenCallableReturnsNull() throws Throwable {
		assertEquals("X", Try.of(() -> null).getOrThrow("X"));
	}

	@Test
	public void ifPresentShouldReturnFalseWhenCallableReturnsNull() {
		assertFalse(Try.of(() -> null).isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldNotCallConsumerWhenCallableReturnsNull() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldNotCallConsumerWhenCallableReturnsNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).ifPresentOrThrow(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifFailureShouldNotCallConsumerWhenCallableReturnsNull() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> null).ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwExceptionShouldNotThrowExceptionWhenCallableReturnsNull() throws Throwable {
		Try.of(() -> null).throwException();
	}

	@Test
	public void valueShouldReturnEmptyOptionalWhenCallableReturnsNull()  {
		assertFalse(Try.of(() -> null).value().isPresent());
	}

	@Test
	public void mapShouldNotCallFunctionWhenCallableReturnsNull() {
		@SuppressWarnings("unchecked")
		Function<Object, Object> function = mock(Function.class);
		Try.of(() -> null).map(function).getOrElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void flatMapShouldNotCallFunctionWhenCallableReturnsNull() {
		@SuppressWarnings("unchecked")
		Function<Object, Try<Object, Throwable>> function = mock(Function.class);
		Try.of(() -> null).flatMap(function).getOrElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldCallSuccessConsumerWhenCallableReturnsNull() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).onSuccess(consumer).getOrElse(null);
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldNotCallFailureConsumerWhenCallableReturnsNull() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> null).onFailure(consumer).getOrElse(null);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void isFailureShouldReturnFalseWhenCallableReturnsValue() {
		assertFalse(Try.of(() -> "X").isFailure());
	}

	@Test
	public void getShouldReturnValueWhenCallableReturnsValue() {
		assertEquals("X", Try.of(() -> "X").get());
	}

	@Test
	public void getOrElseShouldReturnValueWhenCallableReturnsValue() {
		assertEquals("X", Try.of(() -> "X").getOrElse("Y"));
	}

	@Test
	public void getOrThrowShouldReturnValueWhenCallableReturnsValue() throws Throwable {
		assertEquals("X", Try.of(() -> "X").getOrThrow());
	}

	@Test
	public void getOrThrowWithDefaultValueShouldReturnValueWhenCallableReturnsValue() throws Throwable {
		assertEquals("X", Try.of(() -> "X").getOrThrow("Y"));
	}

	@Test
	public void isPresentShouldReturnTrueWhenCallableReturnsValue() {
		assertTrue(Try.of(() -> "X").isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldCallConsumerWhenCallableReturnsValue() {
		@SuppressWarnings("unchecked")
		Consumer<String> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifPresent(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldCallConsumerWhenCallableReturnsValue() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<String> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifPresentOrThrow(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifFailureShouldNotCallConsumerWhenCallableReturnsValue() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwExceptionShouldNotThrowExceptionWhenCallableReturnsValue() throws Throwable {
		Try.of(() -> "X").throwException();
	}

	@Test
	public void valueShouldReturnNotEmptyOptionalWhenCallableReturnsValue() {
		assertTrue(Try.of(() -> "X").value().isPresent());
	}

	@Test
	public void mapShouldCallFunctionWhenCallableReturnsValue() {
		@SuppressWarnings("unchecked")
		Function<String, Object> function = mock(Function.class);
		when(function.apply("X")).thenReturn("Y");
		Try.of(() -> "X").map(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void flatMapShouldCallFunctionWhenCallableReturnsValue() {
		@SuppressWarnings("unchecked")
		Function<String, Try<Object, Throwable>> function = mock(Function.class);
		when(function.apply("X")).thenReturn(Try.of(() -> "Y"));
		Try.of(() -> "X").flatMap(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void mapShouldReturnFailureWhenFunctionThrowsException() {
		@SuppressWarnings("unchecked")
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.of(() -> "X").map(function).isFailure());
	}

	@Test
	public void flatMapShouldReturnFailureWhenFunctionThrowsException() {
		@SuppressWarnings("unchecked")
		Function<String, Try<Object, Throwable>> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.of(() -> "X").flatMap(function).isFailure());
	}

	@Test
	public void mapShouldNotReturnFailureWhenFunctionReturnsNull() {
		@SuppressWarnings("unchecked")
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		assertFalse(Try.of(() -> "X").map(function).isFailure());
	}

	@Test
	public void flatMapShouldNotReturnFailureWhenFunctionReturnsNull() {
		@SuppressWarnings("unchecked")
		Function<String, Try<Object, Throwable>> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		assertFalse(Try.of(() -> "X").flatMap(function).isFailure());
	}

	@Test
	public void shouldCallSuccessConsumerWhenCallableReturnsValue() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> "X").onSuccess(consumer).get();
		verify(consumer, times(1)).accept(anyString());
	}

	@Test
	public void shouldNotCallFailureConsumerWhenCallableReturnsValue() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> "X").onFailure(consumer);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void isFailureShouldReturnTrueWhenCallableThrowsException() {
		assertTrue(Try.of(() -> { throw new Exception(); }).isFailure());
	}

	@Test
	public void getShouldThrowNoSuchElementExceptionWhenCallableThrowsException() {
		exception.expect(NoSuchElementException.class);
		Try.of(() -> { throw new Exception(); }).get();
	}

	@Test
	public void getOrElseShouldReturnDefaultValueWhenCallableThrowsException() {
		assertEquals("X", Try.of(() -> { throw new Exception(); }).getOrElse("X"));
	}

	@Test
	public void getOrThrowShouldThrowExceptionWhenCallableThrowsException() throws Throwable {
		exception.expect(IllegalAccessException.class);
		Try.of(() -> { throw new IllegalAccessException(); }).getOrThrow();
	}

	@Test
	public void getOrThrowWithDefaultValueShouldThrowExceptionWhenCallableThrowsException() throws Throwable {
		exception.expect(IllegalAccessException.class);
		Try.of(() -> { throw new IllegalAccessException(); }).getOrThrow("X");
	}

	@Test
	public void isPresentShouldReturnFalseWhenCallableThrowsException() {
		assertFalse(Try.of(() -> { throw new Exception(); }).isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldNotCallConsumerWhenCallableThrowsException() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldThrowExceptionWhenCallableThrowsException() throws Throwable {
		exception.expect(Exception.class);
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).ifPresentOrThrow(consumer);
	}

	@Test
	public void ifFailureShouldCallConsumerWhenCallableThrowsException() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).ifFailure(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void throwExceptionShouldThrowExceptionWhenCallableThrowsException() throws Throwable {
		exception.expect(Exception.class);
		Try.of(() -> { throw new Exception(); }).throwException();
	}

	@Test
	public void valueShouldReturnEmptyOptionalWhenCallableThrowsException() {
		assertFalse(Try.of(() -> { throw new Exception(); }).value().isPresent());
	}

	@Test
	public void mapShouldNotCallFunctionWhenCallableThrowsException() {
		@SuppressWarnings("unchecked")
		Function<Object, Object> function = mock(Function.class);
		Try.of(() -> { throw new Exception(); }).map(function).getOrElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void flatMapShouldNotCallFunctionWhenCallableThrowsException() {
		@SuppressWarnings("unchecked")
		Function<Object, Try<Object, Throwable>> function = mock(Function.class);
		Try.of(() -> { throw new Exception(); }).flatMap(function).getOrElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldNotCallSuccessConsumerWhenCallableThrowsException() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onSuccess(consumer).isFailure();
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void shouldCallFailureConsumerWhenCallableThrowsException() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onFailure(consumer).isFailure();
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldNotCallSecondCallableWhenFirstCallableReturnsValue() throws Exception {
		Callable<String> callable = mock(Callable.class);
		Try.of(() -> "X").or(callable);
		verify(callable, times(0)).call();
	}

	@Test
	public void shouldReturnValueOfFirstCallableWhenFirstCallableReturnsValue() {
		Callable<String> callable = mock(Callable.class);
		assertEquals("X", Try.of(() -> "X").or(callable).get());
	}

	@Test
	public void shouldCallSecondCallableWhenFirstCallableThrowsException() throws Exception {
		Callable<Object> callable = mock(Callable.class);
		Try.of(() -> { throw new Exception(); }).or(callable).isPresent();
		verify(callable, times(1)).call();
	}

	@Test
	public void shouldReturnValueOfSecondCallableWhenFirstCallableThrowsExceptionAndSecondCallableReturnsValue() {
		Callable<String> callable = mock(Callable.class);
		assertEquals("Y", Try.of(() -> { throw new Exception(); }).or(() -> "Y").get());
	}

	@Test
	public void shouldReturnFailureWhenAllCallablesThrowException() {
		assertTrue(Try.of(() -> { throw new Exception(); }).or(() -> { throw new Exception(); }).isFailure());
	}

	@Test
	public void shouldReturnIOExceptionWhenCallableThrowsIOException() {
		Consumer<IOException> consumer = mock(Consumer.class);
		Try.of(() -> { throw new IOException(); }).withMapper(testMapper()).ifFailure(consumer);
		verify(consumer, times(1)).accept(any(IOException.class));
	}

	@Test
	public void shouldThrowsIOException() throws IOException {
		exception.expect(IOException.class);
		Try.of(() -> { throw new IOException(); }).withMapper(testMapper()).throwException();
	}

	@Test
	public void shouldThrowSameExceptionWhenCallableThrowsException() throws IOException {
		IOException x = new IOException();
		exception.expect(is(x));
		Try.of(() -> { throw x; }).withMapper(testMapper()).throwException();
	}

	@Test
	public void shouldThrowSameExceptionWithCauseWhenCallableThrowsException() throws IOException {
		NullPointerException x = new NullPointerException();
		exception.expectCause(is(x));
		Try.of(() -> { throw x; }).withMapper(testMapper()).throwException();
	}

	private Function<Throwable, IOException> testMapper() {
		return e -> (e instanceof IOException) ? (IOException)e : new IOException(e);
	}
}
