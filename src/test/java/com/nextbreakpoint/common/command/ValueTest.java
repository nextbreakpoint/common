package com.nextbreakpoint.common.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValueTest {
	@Test
	void shouldNotBeNullWhenValueIsNull() {
		assertThat(Command.value(null)).isNotNull();
	}

	@Test
	void shouldNotBeNullWhenValueIsNotNull() {
		assertThat(Command.value("X")).isNotNull();
	}
}
