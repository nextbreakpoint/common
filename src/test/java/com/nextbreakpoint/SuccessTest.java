package com.nextbreakpoint;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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
