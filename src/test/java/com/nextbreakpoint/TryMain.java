package com.nextbreakpoint;

import java.io.IOException;
import java.util.function.Function;

public class TryMain {
	public static void main(String[] args) {
		Try.of(() -> doSomething()).map(x -> x.toLowerCase()).ifPresent(System.out::println);

		Try.of(() -> doSomething()).filter(v -> "X".equals(v)).ifPresent(System.out::println);

		Try.of(() -> alwaysFail()).ifFailure(TryMain::handleException);

		Try.of(() -> alwaysFail()).mapper(mapper()).ifFailure(TryMain::handleException);

		Try.of(() -> alwaysFail()).or(() -> "Y").ifPresent(System.out::println);
	}

	private static Function<Exception, IOException> mapper() {
		return e -> (e instanceof IOException) ? (IOException)e : new IOException("IO Error", e);
	}

	private static void handleException(Exception e) {
		System.out.println("Exception: " + e.getMessage());
	}

	private static void handleException(IOException e) {
		System.out.println("IOException: " + e.getMessage());
	}

	public static String doSomething() throws Exception {
		return "X";
	}

	public static String alwaysFail() throws Exception {
		throw new Exception("Error");
	}
}
