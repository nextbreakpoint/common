package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class IsSuccessTest {
	@Test
	 void shouldReturnTrueWhenSuccessIsNull() {
		assertThat(Either.success(null).isSuccess()).isTrue();
	}

	@Test
	 void shouldReturnTrueWhenSuccessIsNotNull() {
		assertThat(Either.success("X").isSuccess()).isTrue();
	}

	@Test
	 void shouldReturnFalseWhenFailure() {
		assertThat(Either.failure(new Exception()).isSuccess()).isFalse();
	}
}
