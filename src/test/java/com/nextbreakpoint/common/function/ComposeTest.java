package com.nextbreakpoint.common.function;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class ComposeTest {
    @Test
    void shouldComposeFunctions() throws Exception {
        ThrowingFunction<String, String> func1 = spy(new TestFunction<>("A"));
        ThrowingFunction<String, String> func2 = spy(new TestFunction<>("B"));
        assertThat(func1.compose(func2).apply("C")).isEqualTo("A");
        verify(func1).apply("B");
        verify(func2).apply("C");
    }

    @Test
    void shouldThrowWhenFunctionThrows() {
        final Exception exception = new Exception();
        ThrowingFunction<String, String> func1 = spy(new ErrorFunction<>(exception));
        ThrowingFunction<String, String> func2 = spy(new TestFunction<>("B"));
        assertThatThrownBy(() -> func1.compose(func2).apply("C")).isEqualTo(exception);
    }

    @Test
    void shouldThrowWhenBeforeFunctionThrows() {
        final Exception exception = new Exception();
        ThrowingFunction<String, String> func1 = spy(new TestFunction<>("A"));
        ThrowingFunction<String, String> func2 = spy(new ErrorFunction<>(exception));
        assertThatThrownBy(() -> func1.compose(func2).apply("C")).isEqualTo(exception);
    }

    private static class TestFunction<T, R> implements ThrowingFunction<T, R> {
        private final R output;

        private TestFunction(R output) {
            this.output = output;
        }

        @Override
        public R apply(T value) {
            return output;
        }
    }

    private static class ErrorFunction<T, R> implements ThrowingFunction<T, R> {
        private final Exception exception;

        private ErrorFunction(Exception exception) {
            this.exception = exception;
        }

        @Override
        public R apply(T value) throws Exception {
            throw exception;
        }
    }
}