# Try 1.3.1

Try implements a functional API for handling checked and unchecked exceptions.

## Example

Given the program:

    public class TryMain {
        public static void main(String[] args) {
            Try.of(() -> serviceOK.doSomething()).ifPresent(System.out::println);

            Try.of(() -> serviceKO.doSomething()).or(() -> "Y").ifPresent(System.out::println);

            Try.of(() -> serviceOK.doSomething()).map(x -> x.toLowerCase()).ifPresent(System.out::println);

            Try.of(() -> serviceKO.doSomething()).ifFailure(TryMain::handleException);

            Try.of(mapper(), () -> serviceKO.doSomething()).ifFailure(TryMain::handleException);
        }

        private static final Service serviceOK = new ServiceOK();
        private static final Service serviceKO = new ServiceKO();

        private static Function<Throwable, IOException> mapper() {
            return e -> (e instanceof IOException) ? (IOException)e : new IOException("IO Error", e);
        }

        private static void handleException(Throwable e) {
            System.out.println("Throwable: " + e.getMessage());
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

The output will be:

    X
    Y
    x
    Throwable: Service Error
    IOException: IO Error
