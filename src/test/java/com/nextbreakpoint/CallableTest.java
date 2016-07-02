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
	public void shouldNotReturnNullWhenCallableIsNotNull() {
		assertNotNull(Try.of(() -> "X"));
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenCallableIsNullAndMapperIsNotNull() {
		exception.expect(NullPointerException.class);
		Try.of(e -> e, null);
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenCallableIsNullAndMapperIsNull() {
		exception.expect(NullPointerException.class);
		Try.of(null, null);
	}

	@Test
	public void shouldNotReturnNullWhenCallableIsNotNullAndMapperIsNotNull() {
		assertNotNull(Try.of(e -> e, () -> "X"));
	}

	@Test
	public void shouldNotReturnNullWhenCallableThrowsException() {
		assertNotNull(Try.of(e -> e, () -> { throw new Exception(); }));
	}
	
	@Test
	public void isFailureShouldReturnFalseWhenValueIsNull() {
		assertFalse(Try.of(() -> null).isFailure());
	}

	@Test
	public void getShouldThrowNoSuchElementExceptionWhenValueIsNull() {
		exception.expect(NoSuchElementException.class);
		Try.of(() -> null).get();
	}

	@Test
	public void getOrElseShouldReturnElseValueWhenValueIsNull() {
		assertEquals("X", Try.of(() -> null).getOrElse("X"));
	}

	@Test
	public void getOrThrowShouldThrowNoSuchElementExceptionWhenValueIsNull() throws Throwable {
		exception.expect(NoSuchElementException.class);
		Try.of(() -> null).getOrThrow();
	}

	@Test
	public void getOrThrowWithErrorValueShouldReturnGivenValueWhenValueIsNull() throws Throwable {
		assertEquals("X", Try.of(() -> null).getOrThrow("X"));
	}

	@Test
	public void ifPresentShouldReturnFalseWhenValueIsNull() throws Throwable {
		assertFalse(Try.of(() -> null).isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldNotCallConsumerWhenValueIsNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldNotCallConsumerWhenValueIsNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).ifPresentOrThrow(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifFailureShouldNotCallConsumerWhenValueIsNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> null).ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwExceptionShouldNotThrowExceptionWhenValueIsNull() throws Throwable {
		Try.of(() -> null).throwException();
	}

	@Test
	public void valueShouldReturnEmptyOptionalWhenValueIsNull() throws Throwable {
		assertFalse(Try.of(() -> null).value().isPresent());
	}

	@Test
	public void mapShouldNotCallFunctionWhenValueIsNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<Object, Object> function = mock(Function.class);
		Try.of(() -> null).map(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void flatMapShouldNotCallFunctionWhenValueIsNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<Object, Try<Object, Throwable>> function = mock(Function.class);
		Try.of(() -> null).flatMap(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void onSuccessShouldNotCallConsumerWhenValueIsNull() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> null).onSuccess(consumer);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void onFailureShouldNotCallConsumerWhenValueIsNull() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> null).onFailure(consumer);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void isFailureShouldReturnFalseWhenValueIsNotNull() {
		assertFalse(Try.of(() -> "X").isFailure());
	}

	@Test
	public void getShouldReturnValueWhenValueIsNotNull() {
		assertEquals("X", Try.of(() -> "X").get());
	}

	@Test
	public void getOrElseShouldReturnValueWhenValueIsNotNull() {
		assertEquals("X", Try.of(() -> "X").getOrElse("Y"));
	}

	@Test
	public void getOrThrowShouldReturnValueWhenValueIsNotNull() throws Throwable {
		assertEquals("X", Try.of(() -> "X").getOrThrow());
	}

	@Test
	public void getOrThrowWithErrorValueShouldReturnValueWhenValueIsNotNull() throws Throwable {
		assertEquals("X", Try.of(() -> "X").getOrThrow("Y"));
	}

	@Test
	public void isPresentShouldReturnTrueWhenValueIsNotNull() throws Throwable {
		assertTrue(Try.of(() -> "X").isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldCallConsumerWhenValueIsNotNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<String> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifPresent(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldCallConsumerWhenValueIsNotNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<String> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifPresentOrThrow(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifFailureShouldNotCallConsumerWhenValueIsNotNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> "X").ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwExceptionShouldNotThrowExceptionWhenValueIsNotNull() throws Throwable {
		Try.of(() -> "X").throwException();
	}

	@Test
	public void valueShouldReturnNotEmptyOptionalWhenValueIsNotNull() throws Throwable {
		assertTrue(Try.of(() -> "X").value().isPresent());
	}

	@Test
	public void mapShouldCallFunctionWhenValueIsNotNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<String, Object> function = mock(Function.class);
		Try.of(() -> "X").map(function);
		verify(function, times(1)).apply("X");
	}

	@Test
	public void flatMapShouldCallFunctionWhenValueIsNotNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<String, Try<Object, Throwable>> function = mock(Function.class);
		Try.of(() -> "X").flatMap(function);
		verify(function, times(1)).apply("X");
	}

	@Test
	public void mapShouldReturnFailureWhenFunctionThrowsException() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.of(() -> "X").map(function).isFailure());
	}

	@Test
	public void flatMapShouldReturnFailureWhenFunctionThrowsException() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<String, Try<Object, Throwable>> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.of(() -> "X").flatMap(function).isFailure());
	}

	@Test
	public void onSuccessShouldCallConsumerWhenValueIsNotNull() {
		@SuppressWarnings("unchecked")
		Consumer<String> consumer = mock(Consumer.class);
		Try.of(() -> "X").onSuccess(consumer);
		verify(consumer, times(1)).accept(anyString());
	}

	@Test
	public void onFailureShouldNotCallConsumerWhenValueIsNotNull() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> "X").onFailure(consumer);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void isFailureShouldReturnTrueWhenCallableThrowsException() {
		assertTrue(Try.of(() -> { throw  new Exception(); }).isFailure());
	}

	@Test
	public void getShouldThrowNoSuchElementExceptionWhenCallableThrowsException() {
		exception.expect(NoSuchElementException.class);
		Try.of(() -> { throw  new Exception(); }).get();
	}

	@Test
	public void getOrElseShouldReturnElseValueWhenCallableThrowsException() {
		assertEquals("X", Try.of(() -> { throw  new Exception(); }).getOrElse("X"));
	}

	@Test
	public void getOrThrowShouldThrowExceptionWhenCallableThrowsException() throws Throwable {
		exception.expect(IllegalAccessException.class);
		Try.of(() -> { throw  new IllegalAccessException(); }).getOrThrow();
	}

	@Test
	public void getOrThrowWithValueShouldThrowExceptionWhenCallableThrowsException() throws Throwable {
		exception.expect(IllegalAccessException.class);
		Try.of(() -> { throw  new IllegalAccessException(); }).getOrThrow("X");
	}

	@Test
	public void isPresentShouldReturnFalseWhenCallableThrowsException() throws Throwable {
		assertFalse(Try.of(() -> { throw  new Exception(); }).isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldNotCallConsumerWhenCallableThrowsException() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw  new Exception(); }).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldThrowExceptionWhenCallableThrowsException() throws Throwable {
		exception.expect(Exception.class);
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw  new Exception(); }).ifPresentOrThrow(consumer);
	}

	@Test
	public void ifFailureShouldCallConsumerWhenCallableThrowsException() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> { throw  new Exception(); }).ifFailure(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void throwExceptionShouldThrowExceptionWhenCallableThrowsException() throws Throwable {
		exception.expect(Exception.class);
		Try.of(() -> { throw  new Exception(); }).throwException();
	}

	@Test
	public void valueShouldReturnEmptyOptionalWhenCallableThrowsException() throws Throwable {
		assertFalse(Try.of(() -> { throw  new Exception(); }).value().isPresent());
	}

	@Test
	public void mapShouldNotCallFunctionWhenCallableThrowsException() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<Object, Object> function = mock(Function.class);
		Try.of(() -> { throw  new Exception(); }).map(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void flatMapShouldNotCallFunctionWhenCallableThrowsException() throws Throwable {
		@SuppressWarnings("unchecked")
		Function<Object, Try<Object, Throwable>> function = mock(Function.class);
		Try.of(() -> { throw  new Exception(); }).flatMap(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void onSuccessShouldNotCallConsumerWhenCallableThrowsException() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.of(() -> { throw  new Exception(); }).onSuccess(consumer);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void onFailureShouldCallConsumerWhenCallableThrowsException() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.of(() -> { throw  new Exception(); }).onFailure(consumer);
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
		Try.of(() -> { throw new Exception(); }).or(callable);
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
		Try.of(testMapper(), () -> { throw new IOException(); }).ifFailure(consumer);
		verify(consumer, times(1)).accept(any(IOException.class));
	}

	@Test
	public void shouldThrowSameExceptionWhenCallableThrowsException() throws IOException {
		IOException x = new IOException();
		exception.expect(is(x));
		Try.of(testMapper(), () -> { throw x; }).throwException();
	}

	@Test
	public void shouldThrowSameExceptionWithCauseWhenCallableThrowsException() throws IOException {
		NullPointerException x = new NullPointerException();
		exception.expectCause(is(x));
		Try.of(testMapper(), () -> { throw x; }).throwException();
	}

	private Function<Throwable, IOException> testMapper() {
		return e -> (e instanceof IOException) ? (IOException)e : new IOException(e);
	}
}
