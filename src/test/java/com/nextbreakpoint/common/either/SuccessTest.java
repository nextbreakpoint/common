package com.nextbreakpoint.common.either;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class SuccessTest {
	@Test
	 void shouldNotBeNullWhenValueIsNull() {
		assertThat(Either.success(null)).isNotNull();
	}

	@Test
	 void shouldNotBeNullWhenValueIsNotNull() {
		assertThat(Either.success("X")).isNotNull();
	}
}
