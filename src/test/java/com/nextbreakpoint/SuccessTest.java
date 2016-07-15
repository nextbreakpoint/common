package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

public class SuccessTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldNotBeNullWhenValueIsNull() {
		assertNotNull(Try.success(null));
	}

	@Test
	public void shouldNotBeNullWhenValueIsNotNull() {
		assertNotNull(Try.success("X"));
	}

	@Test
	public void shouldNotBeNullWhenValueIsNullAndMapperIsNotNull() {
		assertNotNull(Try.success(null).mapper(e -> e));
	}

	@Test
	public void shouldNotBeNullWhenValueIsNotNullAndMapperIsNotNull() {
		assertNotNull(Try.success("X").mapper(e -> e));
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenMapperIsNull() {
		exception.expect(NullPointerException.class);
		Try.success("X").mapper(null);
	}

	@Test
	public void isFailureShouldReturnFalseWhenValueIsNull() {
		assertFalse(Try.success(null).isFailure());
	}

	@Test
	public void isSuccessShouldReturnTrueWhenValueIsNull() {
		assertTrue(Try.success(null).isSuccess());
	}

	@Test
	public void getShouldThrowNoSuchElementExceptionWhenValueIsNull() {
		exception.expect(NoSuchElementException.class);
		Try.success(null).get();
	}

	@Test
	public void orElseShouldReturnDefaultWhenValueIsNull() {
		assertEquals("X", Try.success(null).orElse("X"));
	}

	@Test
	public void orElseGetShouldReturnSupplierValueWhenValueIsNull() {
		assertEquals("X", Try.success(null).orElseGet(() -> "X"));
	}

	@Test
	public void orElseGetShouldReturnNullValueWhenValueIsNullAndSupplierReturnsNull() {
		assertNull(Try.success(null).orElseGet(() -> null));
	}

	@Test
	public void orThrowShouldThrowNoSuchElementExceptionWhenValueIsNull() throws Exception {
		exception.expect(NoSuchElementException.class);
		Try.success(null).orThrow();
	}

	@Test
	public void orThrowWithDefaultValueShouldReturnDefaultValueWhenValueIsNull() throws Exception {
		assertEquals("X", Try.success(null).orThrow("X"));
	}

	@Test
	public void ifPresentShouldReturnFalseWhenValueIsNull() {
		assertFalse(Try.success(null).isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldNotCallConsumerWhenValueIsNull() {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.success(null).ifPresent(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldNotCallConsumerWhenValueIsNull() throws Exception {
		Consumer<Object> consumer = mock(Consumer.class);
		Try.success(null).ifPresentOrThrow(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifFailureShouldNotCallConsumerWhenValueIsNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.success(null).ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifSuccessShouldCallConsumerWhenValueIsNull() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.success(null).ifSuccess(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void throwExceptionShouldNotThrowExceptionWhenValueIsNull() throws Exception {
		Try.success(null).throwIfFailure();
	}

	@Test
	public void valueShouldReturnEmptyOptionalWhenValueIsNull() {
		assertFalse(Try.success(null).value().isPresent());
	}

	@Test
	public void shouldNotCallFunctionForMapWhenValueIsNull() {
		Function<Object, Object> function = mock(Function.class);
		when(function.apply(null)).thenReturn("Y");
		Try.success(null).map(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldNotCallFunctionForFlatMapWhenValueIsNull() {
		Function<Object, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply(null)).thenReturn(Try.success("Y"));
		Try.success(null).flatMap(function).orElse(null);
		verify(function, times(0)).apply(any());
	}

	@Test
	public void shouldCallSuccessConsumerWhenValueIsNull() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.success(null).onSuccess(consumer).orElse(null);
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldNotCallFailureConsumerWhenValueIsNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.success(null).onFailure(consumer).orElse(null);
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void shouldNotCallSecondCallableWhenValueIsNull() throws Exception {
		Callable<Object> callable = mock(Callable.class);
		Try.success(null).or(callable).isPresent();
		verify(callable, times(0)).call();
	}

	@Test
	public void isFailureShouldReturnFalseWhenValueIsNotNull() {
		assertFalse(Try.success("X").isFailure());
	}

	@Test
	public void isSuccessShouldReturnTrueWhenValueIsNotNull() {
		assertTrue(Try.success("X").isSuccess());
	}

	@Test
	public void getShouldReturnValueWhenValueIsNotNull() {
		assertEquals("X", Try.success("X").get());
	}

	@Test
	public void orElseShouldReturnValueWhenValueIsNotNull() {
		assertEquals("X", Try.success("X").orElse("Y"));
	}

	@Test
	public void orElseGetShouldReturnValueWhenValueIsNotNull() {
		assertEquals("X", Try.success("X").orElseGet(() -> "X"));
	}

	@Test
	public void orElseGetShouldReturnValueWhenValueIsNotNullAndSupplierReturnsNull() {
		assertEquals("X", Try.success("X").orElseGet(() -> null));
	}

	@Test
	public void orThrowShouldReturnValueWhenValueIsNotNull() throws Exception {
		assertEquals("X", Try.success("X").orThrow());
	}

	@Test
	public void orThrowWithDefaultValueShouldReturnValueWhenValueIsNotNull() throws Exception {
		assertEquals("X", Try.success("X").orThrow("Y"));
	}

	@Test
	public void isPresentShouldReturnTrueWhenValueIsNotNull() {
		assertTrue(Try.success("X").isPresent());
	}

	@Test
	public void ifPresentWithConsumerShouldCallConsumerWhenValueIsNotNull() {
		Consumer<String> consumer = mock(Consumer.class);
		Try.success("X").ifPresent(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifPresentOrThrowShouldCallConsumerWhenValueIsNotNull() throws Exception {
		Consumer<String> consumer = mock(Consumer.class);
		Try.success("X").ifPresentOrThrow(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void ifFailureShouldNotCallConsumerWhenValueIsNotNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.success("X").ifFailure(consumer);
		verify(consumer, times(0)).accept(any());
	}

	@Test
	public void ifSuccessShouldCallConsumerWhenValueIsNotNull() {
		Consumer<Optional<String>> consumer = mock(Consumer.class);
		Try.success("X").ifSuccess(consumer);
		verify(consumer, times(1)).accept(any());
	}

	@Test
	public void throwExceptionShouldNotThrowExceptionWhenValueIsNotNull() throws Exception {
		Try.success("X").throwIfFailure();
	}

	@Test
	public void valueShouldReturnNotEmptyOptionalWhenValueIsNotNull() {
		assertTrue(Try.success("X").value().isPresent());
	}

	@Test
	public void shouldCallFunctionForMapWhenValueIsNotNull() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply("X")).thenReturn("Y");
		Try.success("X").map(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void shouldCallFunctionForFlatMapWhenValueIsNotNull() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply("X")).thenReturn(Try.success("Y"));
		Try.success("X").flatMap(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void shouldReturnFailureWhenFunctionForMapThrowsException() {
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.success("X").map(function).isFailure());
	}

	@Test
	public void shouldReturnFailureWhenFunctionForFlatMapThrowsException() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply(any())).thenThrow(RuntimeException.class);
		assertTrue(Try.success("X").flatMap(function).isFailure());
	}

	@Test
	public void shouldThrowNoSuchElementExceptionWhenFunctionForMapReturnsNull() {
		exception.expect(NoSuchElementException.class);
		Function<String, Object> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		Try.success("X").map(function).get();
	}

	@Test
	public void shouldThrowNoSuchElementExceptionWhenFunctionForFlatMapReturnsNull() {
		exception.expect(NoSuchElementException.class);
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		when(function.apply(any())).thenReturn(null);
		Try.success("X").flatMap(function).get();
	}

	@Test
	public void shouldCallFilterForMapWhenValueIsNotNull() {
		Function<String, Object> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn("Y");
		when(filter.test("X")).thenReturn(true);
		Try.success("X").filter(filter).map(function).get();
		verify(filter, times(1)).test("X");
	}

	@Test
	public void shouldCallFilterForFlatMapWhenValueIsNotNull() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn(Try.success("Y"));
		when(filter.test("X")).thenReturn(true);
		Try.success("X").filter(filter).flatMap(function).get();
		verify(filter, times(1)).test("X");
	}

	@Test
	public void shouldCallFunctionForMapWhenFilterReturnsTrue() {
		Function<String, Object> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn("Y");
		when(filter.test("X")).thenReturn(true);
		Try.success("X").filter(filter).map(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void shouldCallFunctionForFlatMapWhenFilterReturnsTrue() {
		Function<String, Try<Object, Exception>> function = mock(Function.class);
		Predicate<Object> filter = mock(Predicate.class);
		when(function.apply("X")).thenReturn(Try.success("Y"));
		when(filter.test("X")).thenReturn(true);
		Try.success("X").filter(filter).flatMap(function).get();
		verify(function, times(1)).apply("X");
	}

	@Test
	public void shouldCallFilterWhenValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(true);
		Try.success("X").filter(filter).get();
		verify(filter, times(1)).test("X");
	}

	@Test
	public void shouldNotReturnValueWhenFilterReturnsFalseAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(false);
		assertFalse(Try.success("X").filter(filter).isPresent());
	}

	@Test
	public void shouldHaveValueWhenFilterReturnsTrueAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertTrue(Try.success("X").map(v -> v.toLowerCase()).filter(filter).isPresent());
	}

	@Test
	public void shouldNotHaveValueWhenFilterReturnsFalseBeforeMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertFalse(Try.success("X").filter(filter).map(v -> v.toLowerCase()).isPresent());
	}

	@Test
	public void shouldNotHaveValueWhenFilterReturnsFalseAfterMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(true);
		assertFalse(Try.success("X").map(v -> v.toLowerCase()).filter(filter).isPresent());
	}

	@Test
	public void shouldReturnValueWhenFilterIsAfterMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("x")).thenReturn(true);
		assertEquals(Try.success("X").map(v -> v.toLowerCase()).filter(filter).get(), "x");
	}

	@Test
	public void shouldReturnValueWhenFilterIsBeforeMapAndValueIsNotNull() {
		Predicate<Object> filter = mock(Predicate.class);
		when(filter.test("X")).thenReturn(true);
		assertEquals(Try.success("X").filter(filter).map(v -> v.toLowerCase()).get(), "x");
	}

	@Test
	public void shouldCallSuccessConsumerWhenValueIsNotNull() {
		Consumer<Optional<Object>> consumer = mock(Consumer.class);
		Try.success("X").onSuccess(consumer).isPresent();
		verify(consumer, times(1)).accept(anyObject());
	}

	@Test
	public void shouldNotCallFailureConsumerWhenValueIsNotNull() {
		Consumer<Exception> consumer = mock(Consumer.class);
		Try.success("X").onFailure(consumer).isPresent();
		verify(consumer, times(0)).accept(anyObject());
	}

	@Test
	public void shouldNotCallSecondCallableWhenValueIsNotNull() throws Exception {
		Callable<String> callable = mock(Callable.class);
		Try.success("X").or(callable).isPresent();
		verify(callable, times(0)).call();
	}
}
