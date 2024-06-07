package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class OrElseTest {
	@Test
	 void shouldReturnValueWhenSuccessIsNotNull() {
		assertThat(Either.success("X").orElse("Y")).isEqualTo("X");
	}

	@Test
	 void shouldReturnDefaultValueWhenSuccessIsNull() {
		assertThat(Either.success(null).orElse("Y")).isEqualTo("Y");
	}

	@Test
	 void shouldReturnDefaultValueWhenFailure() {
		assertThat(Either.failure(new Exception()).orElse("Y")).isEqualTo("Y");
	}
}
