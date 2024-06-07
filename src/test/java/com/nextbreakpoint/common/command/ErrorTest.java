package com.nextbreakpoint.common.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ErrorTest {
	@Test
	void shouldThrowNullPointerExceptionWhenExceptionIsNull() {
		assertThatThrownBy(() -> Command.error(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void shouldNotBeNullWhenExceptionIsNotNull() {
		assertThat(Command.error(new Exception())).isNotNull();
	}
}
