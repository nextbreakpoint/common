package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class OrTest {
	@Test
	 void shouldReturnValueWhenSuccessIsNotNull() {
		assertThat(Either.success("X").or(() -> Either.success("Y")).get()).isEqualTo("X");
	}

	@Test
	 void shouldReturnValueWhenSuccessIsNull() {
		assertThat(Either.success(null).or(() -> Either.success("Y")).get()).isNull();
	}

	@Test
	 void shouldReturnAlternativeValueWhenFailure() {
        assertThat(Either.failure(new Exception()).or(() -> Either.success("Y")).get()).isEqualTo("Y");
	}

	@Test
	 void shouldReturnAlternativeStatusWhenFailure() {
		assertThat(Either.failure(new Exception()).or(() -> Either.failure(new Exception())).isFailure()).isTrue();
	}
}
