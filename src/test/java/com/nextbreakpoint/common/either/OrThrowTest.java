package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

 class OrThrowTest {
	@Test
	 void shouldReturnValueWhenSuccessIsNotNull() throws Exception {
		assertThat(Either.success("X").orThrow().get()).isEqualTo("X");
	}

	@Test
	 void shouldReturnDefaultValueWhenSuccessIsNull() throws Exception {
		assertThat(Either.success(null).orThrow().get()).isNull();
	}

	@Test
	 void shouldThrowExceptionWhenFailure() {
		final Exception exception = new Exception();
		assertThatThrownBy(() -> Either.failure(exception).orThrow().get()).isEqualTo(exception);
	}

	@Test
	 void shouldThrowTransformedExceptionWhenFailure() {
		final Exception exception = new Exception();
		assertThatThrownBy(() -> Either.failure(exception).orThrow(OrThrowTest::transform).get())
				.isInstanceOf(IllegalStateException.class)
				.hasCause(exception);
	}

	private static IllegalStateException transform(Exception e) {
		return new IllegalStateException(e);
	}
}
