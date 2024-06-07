package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class ExceptionTest {
	@Test
	 void shouldReturnNullWhenSuccessIsNull() {
		assertThat(Either.success(null).exception()).isNull();;
	}

	@Test
	 void shouldReturnNullWhenSuccessIsNotNull() {
		assertThat(Either.success("X").exception()).isNull();
	}

	@Test
	 void shouldReturnNotNullWhenFailure() {
		final Exception exception = new Exception();
		assertThat(Either.failure(exception).exception()).isEqualTo(exception);
	}
}
