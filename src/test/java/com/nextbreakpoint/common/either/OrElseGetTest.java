package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class OrElseGetTest {
	@Test
	 void shouldReturnValueWhenSuccessIsNotNull() {
		assertThat(Either.success("X").orElseGet(() -> "Y")).isEqualTo("X");
	}

	@Test
	 void shouldReturnDefaultValueWhenSuccessIsNull() {
		assertThat(Either.success(null).orElseGet(() -> "Y")).isEqualTo("Y");
	}

	@Test
	 void shouldReturnDefaultValueWhenFailure() {
		assertThat(Either.failure(new Exception()).orElseGet(() -> "Y")).isEqualTo("Y");
	}
}
