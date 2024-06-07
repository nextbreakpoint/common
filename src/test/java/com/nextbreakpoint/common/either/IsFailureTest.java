package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class IsFailureTest {
	@Test
	 void shouldReturnFalseWhenSuccessIsNull() {
		assertThat(Either.success(null).isFailure()).isFalse();
	}

	@Test
	 void shouldReturnFalseWhenSuccessIsNotNull() {
		assertThat(Either.success("X").isFailure()).isFalse();
	}

	@Test
	 void shouldReturnTrueWhenFailure() {
		assertThat(Either.failure(new Exception()).isFailure()).isTrue();
	}
}
