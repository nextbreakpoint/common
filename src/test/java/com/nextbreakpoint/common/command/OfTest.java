package com.nextbreakpoint.common.command;

import com.nextbreakpoint.common.either.Either;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OfTest {
	@Test
	void shouldThrowNullPointerExceptionWhenCallableIsNull() {
		assertThatThrownBy(() -> Command.of((Callable<Object>) null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void shouldThrowNullPointerExceptionWhenEitherIsNull() {
		assertThatThrownBy(() -> Command.of((Either<Object>) null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void shouldNotBeNullWhenCallableIsNotNullAndReturnsValue() {
		assertThat(Command.of(() -> "X")).isNotNull();
		assertThat(Command.of(() -> "X").execute().isSuccess()).isTrue();
		assertThat(Command.of(() -> "X").execute().get()).isEqualTo("X");
	}

	@Test
	void shouldNotBeNullWhenCallableIsNotNullAndReturnsNull() {
		assertThat(Command.of(() -> null)).isNotNull();
		assertThat(Command.of(() -> null).execute().isSuccess()).isTrue();
		assertThat(Command.of(() -> null).execute().get()).isNull();
	}

	@Test
	void shouldNotBeNullWhenCallableIsNotNullAndThrowsException() {
		final Exception exception = new Exception();
		assertThat(Command.of(() -> { throw exception; })).isNotNull();
		assertThat(Command.of(Either.failure(exception)).execute().isFailure()).isTrue();
		assertThat(Command.of(Either.failure(exception)).execute().exception()).isEqualTo(exception);
	}

	@Test
	void shouldNotBeNullWhenEitherIsNotNullAndIsSuccessWithNotNullValue() {
		assertThat(Command.of(Either.success("X"))).isNotNull();
		assertThat(Command.of(Either.success("X")).execute().isSuccess()).isTrue();
		assertThat(Command.of(Either.success("X")).execute().get()).isEqualTo("X");
	}

	@Test
	void shouldNotBeNullWhenEitherIsNotNullAndIsSuccessWithNullValue() {
		assertThat(Command.of(Either.success(null))).isNotNull();
		assertThat(Command.of(Either.success(null)).execute().isSuccess()).isTrue();
		assertThat(Command.of(Either.success(null)).execute().get()).isNull();
	}

	@Test
	void shouldNotBeNullWhenEitherIsNotNullAndIsFailure() {
		final Exception exception = new Exception();
		assertThat(Command.of(Either.failure(exception))).isNotNull();
		assertThat(Command.of(Either.failure(exception)).execute().isFailure()).isTrue();
		assertThat(Command.of(Either.failure(exception)).execute().exception()).isEqualTo(exception);
	}
}
