package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class OptionalTest {
	@Test
	 void shouldReturnTrueWhenSuccessIsNull() {
		assertThat(Either.success(null).optional()).isNotPresent();
	}

	@Test
	 void shouldReturnTrueWhenSuccessIsNotNull() {
		assertThat(Either.success("X").optional()).isPresent();
	}

	@Test
	 void shouldReturnFalseWhenFailure() {
		assertThat(Either.failure(new Exception()).optional()).isNotPresent();
	}
}
