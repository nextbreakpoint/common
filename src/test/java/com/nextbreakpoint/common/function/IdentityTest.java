package com.nextbreakpoint.common.function;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentityTest {
    @Test
    void shouldReturnInputValue() throws Exception {
        final ThrowingFunction<String, String> identity = ThrowingFunction.identity();
        assertThat(identity.apply("X")).isEqualTo("X");
        assertThat(identity.apply("Y")).isEqualTo("Y");
    }
}