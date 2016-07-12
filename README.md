# Try 2.0.0

Try implements a monad for handling checked and unchecked exceptions.

If you are familiar with Stream and Optional classes in Java 8, you will find Try very useful.

    Version 2.x is not backward compatible with versions 1.x.

Version 2.x introduces a new lazy execution, so the callable is executed only when one of the terminal operations is invoked.
The terminal operations are represented by all not static methods which not return a subclass of Try.

The interesting consequence of this behaviour is that you can execute multiple times the same callable.
You can store any instance of Try and reuse it many times to perform the same operations.
   

## Better exceptions handling 

A typical fragment of code for handling exceptions in Java looks like:

    try {
        doSomething();
    } catch (SomeException e) {
        handleException(e);
    }
 
Using Try you can rewrite the same fragment of code in just one line:

    Try.of(() -> doSomething()).ifFailure(e -> handleException(e));


## Getting values


## Mapping and filtering


## Remapping exceptions


## Alternative executions


## Other examples

Given the program:

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

The output will be:

    X
    Y
    x
    Throwable: Service Error
    IOException: IO Error
