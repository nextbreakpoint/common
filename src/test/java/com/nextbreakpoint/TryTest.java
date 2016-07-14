package com.nextbreakpoint;

import org.junit.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class TryTest {
	@Test
	public void testAll() {
		Consumer<Optional<Object>> consumer = s -> { println(s.orElse(null)); };
		Consumer<String> consumer0 = s -> { println(s); };
		Consumer<Object> consumer1 = s -> { println(s); };
		Consumer<Exception> consumer2 = s -> { println(s); };

		println(Try.of(() -> "X").onSuccess(consumer).get()); // Invokes the consumer
		println(Try.of(() -> null).onSuccess(consumer).getOrElse(null)); // Invokes the consumer
		println(Try.of(() -> { throw new Exception(); }).onSuccess(consumer).getOrElse(null)); // Doesn't invoke the consumer
		println(Try.of(() -> "X").onFailure(consumer2).get()); // Doesn't invoke the consumer
		println(Try.of(() -> null).onFailure(consumer2).getOrElse(null)); // Doesn't invoke the consumer
		println(Try.of(() -> { throw new Exception(); }).onFailure(consumer2).getOrElse(null)); // Invokes the consumer

		println(Try.of(() -> "X").get()); // Returns X
		println(Try.of(() -> "X").getOrElse("Y")); // Returns X
		try {
			println(Try.of(() -> null).get()); // Throws exception
		} catch (Exception e) {
			e.printStackTrace();
		}
		println(Try.of(() -> null).getOrElse("Y")); // Returns Y
		try {
			println(Try.of(() -> { throw new IOException(); }).get()); // Throws exception
		} catch (Exception e) {
			e.printStackTrace();
		}
		println(Try.of(() -> { throw new IOException(); }).getOrElse("Y")); // Returns Y

		try {
			println(Try.of(() -> "X").getOrThrow()); // Returns X
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			println(Try.of(() -> "X").getOrThrow("Y")); // Returns X
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			println(Try.of(() -> null).getOrThrow()); // Throws exception
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			println(Try.of(() -> null).getOrThrow("Y")); // Returns Y
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			println(Try.of(() -> { throw new IOException(); }).getOrThrow()); // Throws IOException
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			println(Try.of(() -> { throw new IOException(); }).getOrThrow("Y")); // Throws IOException
		} catch (Exception e) {
			e.printStackTrace();
		}

		println(Try.of(() -> "X").isPresent()); // Returns true
		println(Try.of(() -> null).isPresent()); // Returns false
		println(Try.of(() -> { throw new Exception(); }).isPresent()); // Returns false
		println(Try.of(() -> "X").isFailure()); // Returns false
		println(Try.of(() -> null).isFailure()); // Returns false
		println(Try.of(() -> { throw new Exception(); }).isFailure()); // Returns true

		Try.of(() -> "X").ifPresent(consumer0); // Invokes the consumer
		Try.of(() -> null).ifPresent(consumer1); // Doesn't invoke the consumer
		Try.of(() -> { throw new Exception(); }).ifPresent(consumer1); // Doesn't invoke the consumer
		Try.of(() -> "X").ifFailure(consumer2); // Doesn't invoke the consumer
		Try.of(() -> null).ifFailure(consumer2); // Doesn't invoke the consumer
		Try.of(() -> { throw new Exception(); }).ifFailure(consumer2); // Invokes the consumer
		try {
			Try.of(() -> "X").ifPresentOrThrow(consumer0); // Invokes the consumer
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Try.of(() -> null).ifPresentOrThrow(consumer1); // Doesn't invoke the consumer
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Try.of(() -> { throw new Exception(); }).ifPresentOrThrow(consumer1); // Throws exception
		} catch (Exception e) {
			e.printStackTrace();
		}

		Try.of(() -> "X").map(x -> x.toLowerCase()).ifPresent(System.out::println); // Prints x
		println(Try.of(() -> "X").map(x -> null).isPresent()); // Returns false
		println(Try.of(() -> null).map(x -> "X").isPresent()); // Returns false
		println(Try.of(() -> { throw new Exception(); }).map(x -> "X").isPresent()); // Returns false

		Try.of(() -> "X").flatMap(x -> Try.of(() -> "Y")).ifPresent(System.out::println); // Prints Y
		println(Try.of(() -> "X").flatMap(x -> Try.of(() -> { throw new Exception(x); })).isFailure()); // Returns true

		println(Try.of(() -> "X").filter(v -> "Y".equals(v)).isPresent()); // Returns false
		println(Try.of(() -> null).filter(v -> "Y".equals(v)).isPresent()); // Returns false
		println(Try.of(() -> { throw new Exception(); }).filter(v -> true).isPresent()); // Returns false
	}

	private void println(Object s) {
		System.out.println(s);
	}

	private Function<Exception, IOException> testMapper() {
		return e -> (e instanceof IOException) ? (IOException)e : new IOException(e);
	}
}
