package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SuccessTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldNotReturnNullWhenValueIsNull() {
		assertNotNull(Try.success(null));
	}

	@Test
	public void shouldNotReturnNullWhenValueIsNullAndMapperIsNotNull() {
		assertNotNull(Try.success(e -> e, null));
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenMapperIsNull() {
		exception.expect(NullPointerException.class);
		Try.success(null, "X");
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenValueIsNullAndMapperIsNull() {
		exception.expect(NullPointerException.class);
		Try.success(null, null);
	}

	@Test
	public void shouldNotReturnNullWhenValueIsNotNull() {
		assertNotNull(Try.success("X"));
	}

	@Test
	public void shouldNotReturnNullWhenValueIsNotNullAndMapperIsNotNull() {
		assertNotNull(Try.success(e -> e, "X"));
	}

	@Test
	public void isFailureShouldReturnFalseWhenValueIsNull() {
		assertFalse(Try.success(null).isFailure());
	}

	@Test
	public void getShouldThrowNoSuchElementExceptionWhenValueIsNull() {
		exception.expect(NoSuchElementException.class);
		Try.success(null).get();
	}

	@Test
	public void getOrElseShouldReturnElseValueWhenValueIsNull() {
		assertEquals("X", Try.success(null).getOrElse("X"));
	}

	@Test
	public void getOrThrowShouldThrowNoSuchElementExceptionWhenValueIsNull() throws Throwable {
		exception.expect(NoSuchElementException.class);
		Try.success(null).getOrThrow();
	}

	@Test
	public void getOrThrowWithErrorValueShouldReturnGivenValueWhenValueIsNull() throws Throwable {
		assertEquals("X", Try.success(null).getOrThrow("X"));
	}

	@Test
	public void ifPresentShouldReturnFalseWhenValueIsNull() {
		assertFalse(Try.success(null).isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldNotCallConsumerWhenValueIsNull() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.success(null).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldNotCallConsumerWhenValueIsNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.success(null).ifPresentOrThrow(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifFailureShouldNotCallConsumerWhenValueIsNull() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.success(null).ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwExceptionShouldNotThrowExceptionWhenValueIsNull() throws Throwable {
		Try.success(null).throwException();
	}

	@Test
	public void valueShouldReturnEmptyOptionalWhenValueIsNull() {
		assertFalse(Try.success(null).value().isPresent());
	}

	@Test
	public void mapShouldNotCallFunctionWhenValueIsNull() {
		@SuppressWarnings("unchecked")
		Function<Object, Object> function = mock(Function.class);
		Try.success(null).map(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void flatMapShouldNotCallFunctionWhenValueIsNull() {
		@SuppressWarnings("unchecked")
		Function<Object, Try<Object, Throwable>> function = mock(Function.class);
		Try.success(null).flatMap(function);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void onSuccessShouldNotCallConsumerWhenValueIsNull() {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = mock(Consumer.class);
		Try.success(null).onSuccess(consumer);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void onFailureShouldNotCallConsumerWhenValueIsNull() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.success(null).onFailure(consumer);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void isFailureShouldReturnFalseWhenValueIsNotNull() {
		assertFalse(Try.success("X").isFailure());
	}

	@Test
	public void getShouldReturnValueWhenValueIsNotNull() {
		assertEquals("X", Try.success("X").get());
	}

	@Test
	public void getOrElseShouldReturnValueWhenValueIsNotNull() {
		assertEquals("X", Try.success("X").getOrElse("Y"));
	}

	@Test
	public void getOrThrowShouldReturnValueWhenValueIsNotNull() throws Throwable {
		assertEquals("X", Try.success("X").getOrThrow());
	}

	@Test
	public void getOrThrowWithErrorValueShouldReturnValueWhenValueIsNotNull() throws Throwable {
		assertEquals("X", Try.success("X").getOrThrow("Y"));
	}

	@Test
	public void isPresentShouldReturnTrueWhenValueIsNotNull() {
		assertTrue(Try.success("X").isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldCallConsumerWhenValueIsNotNull() {
		@SuppressWarnings("unchecked")
		Consumer<String> consumer = mock(Consumer.class);
		Try.success("X").ifPresent(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldCallConsumerWhenValueIsNotNull() throws Throwable {
		@SuppressWarnings("unchecked")
		Consumer<String> consumer = mock(Consumer.class);
		Try.success("X").ifPresentOrThrow(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifFailureShouldNotCallConsumerWhenValueIsNotNull() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.success("X").ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void throwExceptionShouldNotThrowExceptionWhenValueIsNotNull() throws Throwable {
		Try.success("X").throwException();
	}

	@Test
	public void valueShouldReturnNotEmptyOptionalWhenValueIsNotNull() {
		assertTrue(Try.success("X").value().isPresent());
	}

	@Test
	public void mapShouldCallFunctionWhenValueIsNotNull() {
		@SuppressWarnings("unchecked")
		Function<String, Object> function = mock(Function.class);
		Try.success("X").map(function);
		verify(function, times(1)).apply("X");
	}

	@Test
	public void flatMapShouldCallFunctionWhenValueIsNotNull() {
		@SuppressWarnings("unchecked")
		Function<String, Try<Object, Throwable>> function = mock(Function.class);
		Try.success("X").flatMap(function);
		verify(function, times(1)).apply("X");
	}

	@Test
	public void mapShouldReturnFailureWhenFunctionThrowsException() {
		@SuppressWarnings("unchecked")
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.success("X").map(function).isFailure());
	}

	@Test
	public void flatMapShouldReturnFailureWhenFunctionThrowsException() {
		@SuppressWarnings("unchecked")
		Function<String, Try<Object, Throwable>> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.success("X").flatMap(function).isFailure());
	}

	@Test
	public void onSuccessShouldCallConsumerWhenValueIsNotNull() {
		@SuppressWarnings("unchecked")
		Consumer<String> consumer = mock(Consumer.class);
		Try.success("X").onSuccess(consumer);
		verify(consumer, times(1)).accept(anyString());
	}

	@Test
	public void onFailureShouldNotCallConsumerWhenValueIsNotNull() {
		@SuppressWarnings("unchecked")
		Consumer<Throwable> consumer = mock(Consumer.class);
		Try.success("X").onFailure(consumer);
		verify(consumer, times(0)).accept(anyObject());
	}
}
