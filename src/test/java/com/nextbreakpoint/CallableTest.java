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
	public void getOrThrowShouldThrowNoSuchElementExceptionWhenCallableReturnsNull() throws Exception {
		exception.expect(NoSuchElementException.class);
		Try.of(() -> null).getOrThrow();
	}

	@Test
	public void getOrThrowWithDefaultValueShouldReturnDefaultValueWhenCallableReturnsNull() throws Exception {
		assertEquals("X", Try.of(() -> null).getOrThrow("X"));
	}

	@Test
	public void ifPresentShouldReturnFalseWhenCallableReturnsNull() {
		assertFalse(Try.of(() -> null).isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldNotCallConsumerWhenCallableReturnsNull() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldNotCallConsumerWhenCallableReturnsNull() throws Exception {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).ifPresentOrThrow(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifFailureShouldNotCallConsumerWhenCallableReturnsNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> null).ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwExceptionShouldNotThrowExceptionWhenCallableReturnsNull() throws Exception {
		Try.of(() -> null).throwException();
	}

	@Test
	public void valueShouldReturnEmptyOptionalWhenCallableReturnsNull()  {
		assertFalse(Try.of(() -> null).value().isPresent());
	}

	@Test
	public void mapShouldNotCallFunctionWhenCallableReturnsNull() {
		Function<Object, Object> function = mock(Function.class);
		Try.of(() -> null).map(function).getOrElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void flatMapShouldNotCallFunctionWhenCallableReturnsNull() {
		Function<Object, Try<Object, Exception>> function = mock(Function.class);
		Try.of(() -> null).flatMap(function).getOrElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldCallSuccessConsumerWhenCallableReturnsNull() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).onSuccess(consumer).getOrElse(null);
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldNotCallFailureConsumerWhenCallableReturnsNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
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
	public void getOrThrowShouldReturnValueWhenCallableReturnsValue() throws Exception {
		assertEquals("X", Try.of(() -> "X").getOrThrow());
	}

	@Test
	public void getOrThrowWithDefaultValueShouldReturnValueWhenCallableReturnsValue() throws Exception {
		assertEquals("X", Try.of(() -> "X").getOrThrow("Y"));
	}

	@Test
	public void isPresentShouldReturnTrueWhenCallableReturnsValue() {
		assertTrue(Try.of(() -> "X").isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldCallConsumerWhenCallableReturnsValue() {
		Consumer<String> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifPresent(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldCallConsumerWhenCallableReturnsValue() throws Exception {
		Consumer<String> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifPresentOrThrow(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifFailureShouldNotCallConsumerWhenCallableReturnsValue() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwExceptionShouldNotThrowExceptionWhenCallableReturnsValue() throws Exception {
		Try.of(() -> "X").throwException();
	}

	@Test
	public void valueShouldReturnNotEmptyOptionalWhenCallableReturnsValue() {
		assertTrue(Try.of(() -> "X").value().isPresent());
	}

	@Test
	public void mapShouldCallFunctionWhenCallableReturnsValue() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply("X")).thenReturn("Y");
		Try.of(() -> "X").map(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void flatMapShouldCallFunctionWhenCallableReturnsValue() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply("X")).thenReturn(Try.of(() -> "Y"));
		Try.of(() -> "X").flatMap(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void mapShouldReturnFailureWhenFunctionThrowsException() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.of(() -> "X").map(function).isFailure());
	}

	@Test
	public void flatMapShouldReturnFailureWhenFunctionThrowsException() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.of(() -> "X").flatMap(function).isFailure());
	}

	@Test
	public void mapShouldNotReturnFailureWhenFunctionReturnsNull() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		assertFalse(Try.of(() -> "X").map(function).isFailure());
	}

	@Test
	public void flatMapShouldNotReturnFailureWhenFunctionReturnsNull() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		assertFalse(Try.of(() -> "X").flatMap(function).isFailure());
	}

	@Test
	public void shouldCallSuccessConsumerWhenCallableReturnsValue() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> "X").onSuccess(consumer).get();
		verify(consumer, times(1)).accept(anyString());
	}

	@Test
	public void shouldNotCallFailureConsumerWhenCallableReturnsValue() {
		Consumer<Exception> consumer = mock(Consumer.class);
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
	public void getOrThrowShouldThrowExceptionWhenCallableThrowsException() throws Exception {
		exception.expect(IllegalAccessException.class);
		Try.of(() -> { throw new IllegalAccessException(); }).getOrThrow();
	}

	@Test
	public void getOrThrowWithDefaultValueShouldThrowExceptionWhenCallableThrowsException() throws Exception {
		exception.expect(IllegalAccessException.class);
		Try.of(() -> { throw new IllegalAccessException(); }).getOrThrow("X");
	}

	@Test
	public void isPresentShouldReturnFalseWhenCallableThrowsException() {
		assertFalse(Try.of(() -> { throw new Exception(); }).isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldNotCallConsumerWhenCallableThrowsException() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldThrowExceptionWhenCallableThrowsException() throws Exception {
		exception.expect(Exception.class);
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).ifPresentOrThrow(consumer);
	}

	@Test
	public void ifFailureShouldCallConsumerWhenCallableThrowsException() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).ifFailure(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void throwExceptionShouldThrowExceptionWhenCallableThrowsException() throws Exception {
		exception.expect(Exception.class);
		Try.of(() -> { throw new Exception(); }).throwException();
	}

	@Test
	public void valueShouldReturnEmptyOptionalWhenCallableThrowsException() {
		assertFalse(Try.of(() -> { throw new Exception(); }).value().isPresent());
	}

	@Test
	public void mapShouldNotCallFunctionWhenCallableThrowsException() {
		Function<Object, Object> function = mock(Function.class);
		Try.of(() -> { throw new Exception(); }).map(function).getOrElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void flatMapShouldNotCallFunctionWhenCallableThrowsException() {
		Function<Object, Try<Object, Exception>> function = mock(Function.class);
		Try.of(() -> { throw new Exception(); }).flatMap(function).getOrElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldNotCallSuccessConsumerWhenCallableThrowsException() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw new Exception(); }).onSuccess(consumer).isFailure();
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void shouldCallFailureConsumerWhenCallableThrowsException() {
		Consumer<Exception> consumer = mock(Consumer.class);
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

	private Function<Exception, IOException> testMapper() {
		return e -> (e instanceof IOException) ? (IOException)e : new IOException(e);
	}
}
