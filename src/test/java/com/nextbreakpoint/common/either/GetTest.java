package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class GetTest {
	@Test
	 void shouldReturnNotNullValueWhenSuccessIsNotNull() {
		assertThat(Either.success("X").get()).isEqualTo("X");
	}

	@Test
	 void shouldReturnNullValueWhenSuccessIsNull() {
		assertThat(Either.success(null).get()).isNull();
	}

	@Test
	 void shouldReturnNullValueWhenFailure() {
		assertThat(Either.failure(new Exception()).get()).isNull();
	}
}
