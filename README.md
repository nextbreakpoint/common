# Try 1.3

Try implements a functional API for dealing with checked or unchecked exceptions in Java 8

## Example

Given the program:

	public class TryMain {
		private static final Service serviceOK = new ServiceOK();
		private static final Service serviceKO = new ServiceKO();
		
		public static void main(String[] args) {
			Try.of(() -> serviceOK.doSomething()).ifPresent(System.out::println);
	
			System.out.println(Try.of(() -> serviceKO.doSomething()).getOrElse("Y"));
	
			try {
				Try.of(() -> serviceKO.doSomething()).ifPresentOrThrow(System.out::println);
			} catch (Throwable x) {
				System.out.println(x.getMessage());
			}
	
			Optional<String> value = Try.of(() -> serviceOK.doSomething()).map(x -> x.toUpperCase()).value();
			value.ifPresent(System.out::println);
	
			try {
				System.out.println(Try.of(mapper(), () -> serviceKO.doSomething()).getOrThrow());
			} catch (IOException x) {
				System.out.println(x.getMessage());
			}
		}
	
		private static Function<Throwable, IOException> mapper() {
			return e -> (e instanceof IOException) ? (IOException)e : new IOException("IOError", e);
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
				throw new Exception("Error");
			}
		}
	}

The output will be:

	X
	Y
	Error
	X
	IOError
