# Try 2.0.0

Try implements a monad for handling checked and unchecked exceptions.

If you are familiar with Stream and Optional classes in Java 8, you will find Try very useful.

Version 2.x introduces lazy execution which means that callable is called only when invoking a terminal operation.
The terminal operations are represented by all not static methods which not return a subclass of Try.

The interesting consequence of this behaviour is that a callable might be executed multiple times.
It is possible to store an instance of Try and reuse it many times to perform the same operations.
   
    WARNING: Version 2.x is not backward compatible with versions 1.x.

## Cleaner code 

A typical fragment of code for handling exceptions in Java looks like:

    try {
        doSomething();
    } catch (SomeException e) {
        handleException(e);
    }
 
Using Try you can rewrite the same fragment of code in just one line:

    Try.of(() -> doSomething()).ifFailure(e -> handleException(e));

## Getting values

Use get() or getOrElse() to get the value returned by a callable: 

    Try.of(() -> "X").get(); // Returns X
    Try.of(() -> "X").getOrElse("Y"); // Returns X
    Try.of(() -> null).get(); // Throws exception
    Try.of(() -> null).getOrElse("Y"); // Returns Y
    Try.of(() -> { throw new IOException(); }).get(); // Throws exception
    Try.of(() -> { throw new IOException(); }).getOrElse("Y"); // Returns Y

Use getOrThrow() to get the value or re-throw an exception: 

    Try.of(() -> "X").getOrThrow(); // Returns X
    Try.of(() -> "X").getOrThrow("Y"); // Returns X
    Try.of(() -> null).getOrThrow(); // Throws exception
    Try.of(() -> null).getOrThrow("Y"); // Returns Y
    Try.of(() -> { throw new IOException(); }).getOrThrow(); // Throws IOException
    Try.of(() -> { throw new IOException(); }).getOrThrow("Y"); // Throws IOException

## Checking values

Use isPresent() or isFailure() to programmatically check the result: 

    Try.of(() -> "X").isPresent(); // Returns true
    Try.of(() -> null).isPresent(); // Returns false
    Try.of(() -> { throw new Exception(); }).isPresent(); // Returns false
    Try.of(() -> "X").isFailure(); // Returns false
    Try.of(() -> null).isFailure(); // Returns false
    Try.of(() -> { throw new Exception(); }).isFailure(); // Returns true

Use ifPresent(), ifPresentOrThrow() or ifFailure() to conditionally execute code: 

    Try.of(() -> "X").ifPresent(consumer); // Invokes the consumer
    Try.of(() -> null).ifPresent(consumer); // Doesn't invoke the consumer
    Try.of(() -> { throw new Exception(); }).ifPresent(consumer); // Doesn't invoke the consumer
    Try.of(() -> "X").ifFailure(consumer); // Doesn't invoke the consumer
    Try.of(() -> null).ifFailure(consumer); // Doesn't invoke the consumer
    Try.of(() -> { throw new Exception(); }).ifFailure(consumer); // Invokes the consumer    
    Try.of(() -> "X").ifPresentOrThrow(consumer); // Invokes the consumer
    Try.of(() -> null).ifPresentOrThrow(consumer); // Doesn't invoke the consumer
    Try.of(() -> { throw new Exception(); }).ifPresentOrThrow(consumer); // Throws exception

## Receiving events

Use onSuccess() and onFailure() to receive events:
  
    Try.of(() -> "X").onSuccess(consumer).get(); // Invokes the consumer
    Try.of(() -> null).onSuccess(consumer).getOrElse(null); // Invokes the consumer
    Try.of(() -> { throw new Exception(); }).onSuccess(consumer).getOrElse(null); // Doesn't invoke the consumer
    Try.of(() -> "X").onFailure(consumer).get(); // Doesn't invoke the consumer
    Try.of(() -> null).onFailure(consumer).getOrElse(null); // Doesn't invoke the consumer
    Try.of(() -> { throw new Exception(); }).onFailure(consumer).getOrElse(null); // Invokes the consumer

## Mapping and filtering

Use map() or flatMap() to transform the value:

    Try.of(() -> "X").map(x -> x.toLowerCase()).ifPresent(System.out::println); // Prints x
    Try.of(() -> "X").map(x -> null).isPresent(); // Returns false
    Try.of(() -> null).map(x -> "X").isPresent(); // Returns false
    Try.of(() -> { throw new Exception(); }).map(x -> "X").isPresent(); // Returns false
    Try.of(() -> "X").flatMap(x -> Try.of(() -> "Y")).ifPresent(System.out::println); // Prints Y
    Try.of(() -> "X").flatMap(x -> Try.of(() -> { throw new Exception(x); })).isFailure(); // Returns true
    
Use filter() to filter the value:

    Try.of(() -> "X").filter(v -> "Y".equals(v)).isPresent(); // Returns false
    Try.of(() -> null).filter(v -> v == null).isPresent(); // Returns false
    Try.of(() -> { throw new Exception(); }).filter(v -> true).isPresent(); // Returns false

## Remapping exceptions

Use the mapper() to change type of exception:

    Try.of(() -> { throw new Exception(); }).mapper(mapper()).ifFailure(e -> handleException(e));
    
    private static void handleException(IOException e) {
        System.out.println("IOException: " + e.getMessage());
    }

    private static Function<Exception, IOException> mapper() {
       return e -> (e instanceof IOException) ? (IOException)e : new IOException("IO Error", e);
    }

## Alternative executions

Use or() to provide an alternative callable:

    Try.of(() -> { throw new Exception(); }).or(() -> "X").get(); // Returns X    

## Complete example

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
