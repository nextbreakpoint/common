package com.nextbreakpoint;

public class TryMain {
	public static void main(String[] args) {
		System.out.println(Try.of(e -> e, () -> "X".charAt(1)).getOrElse('Y'));
	}
}
