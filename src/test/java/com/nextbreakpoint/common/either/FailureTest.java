package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

 class FailureTest {
	@Test
	 void shouldThrowNullPointerExceptionWhenExceptionIsNull() {
		assertThatThrownBy(() -> Either.failure(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	 void shouldNotBeNullWhenExceptionIsNotNull() {
		assertThat(Either.failure(new Exception())).isNotNull();
	}
}
