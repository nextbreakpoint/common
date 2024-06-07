package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class StreamTest {
	@Test
	 void shouldReturnTrueWhenSuccessIsNull() {
		assertThat(Either.success(null).stream()).isEmpty();
	}

	@Test
	 void shouldReturnTrueWhenSuccessIsNotNull() {
		assertThat(Either.success("X").stream()).hasSize(1);
	}

	@Test
	 void shouldReturnFalseWhenFailure() {
		assertThat(Either.failure(new Exception()).stream()).isEmpty();
	}
}
