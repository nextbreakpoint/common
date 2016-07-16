package com.nextbreakpoint;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class SuccessTest {
	@Test
	public void shouldNotBeNullWhenValueIsNull() {
		assertNotNull(Try.success(null));
	}

	@Test
	public void shouldNotBeNullWhenValueIsNotNull() {
		assertNotNull(Try.success("X"));
	}
}
