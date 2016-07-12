package com.nextbreakpoint;

import java.io.IOException;
import java.util.function.Function;

public class TryMain {
	public static void main(String[] args) {
		Try.of(() -> serviceOK.doSomething()).ifPresent(System.out::println);

		Try.of(() -> serviceKO.doSomething()).or(() -> "Y").ifPresent(System.out::println);

		Try.of(() -> serviceOK.doSomething()).map(x -> x.toLowerCase()).ifPresent(System.out::println);

		Try.of(() -> serviceKO.doSomething()).ifFailure(TryMain::handleException);

		Try.of(() -> serviceKO.doSomething()).mapper(mapper()).ifFailure(TryMain::handleException);
	}

	private static final Service serviceOK = new ServiceOK();
	private static final Service serviceKO = new ServiceKO();

	private static Function<Exception, IOException> mapper() {
		return e -> (e instanceof IOException) ? (IOException)e : new IOException("IO Error", e);
	}

	private static void handleException(Exception e) {
		System.out.println("Exception: " + e.getMessage());
	}

	private static void handleException(IOException e) {
		System.out.println("IOException: " + e.getMessage());
	}

	private static interface Service {
		String doSomething() throws Exception;
	}

	private static class ServiceOK implements Service {
		public String doSomething() throws Exception {
			return "X";
		}
	}

	private static class ServiceKO implements Service {
		public String doSomething() throws Exception {
			throw new Exception("Service Error");
		}
	}
}
