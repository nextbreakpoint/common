# Try 2.0.1

Try implements a monad for handling checked and unchecked exceptions.
 
    If you are familiar with Stream and Optional classes in Java 8, you will find Try very useful.

Try supports lazy execution which means that a callable is invoked only when invoking a terminal operation.
A terminal operation is represented by any not static method which doesn't returns a subclass of Try.
One interesting consequence of lazy execution is that a callable might be executed multiple times.
Just keep an instance of Try and reuse it many times to perform the same operations.
   
    WARNING: Version 2.x is not backward compatible with version 1.x. Version 1.x is deprecated.

## Cleaner code 

A typical fragment of code for handling exceptions in Java looks like:

    try {
        doSomething();
    } catch (SomeException e) {
        handleException(e);
    }
 
Using Try you can rewrite the same fragment of code in just one line:

    Try.of(() -> doSomething()).ifFailure(e -> handleException(e));
    
The previous code uses lambdas to implement a callable and a consumer: 
    
    () -> doSomething() // Callable 
    
    e -> handleException(e) // Consumer

## Getting binaries

Try is available in Maven Central Repository, Bintray and GitHub. 

If you are using Maven, add a dependency in your POM:

    <dependency>
        <groupId>com.nextbreakpoint</groupId>
        <artifactId>com.nextbreakpoint.try</artifactId>
        <version>2.0.1</version>
    </dependency>

If you are using other tools, check the documentation how to install an artifact.
  
## Getting value

Use get(), orElse(), orElseGet() to get the value returned by a callable: 

    Try.of(() -> "X").get(); // Returns X
    
    Try.of(() -> "X").orElse("Y"); // Returns X
    
    Try.of(() -> "X").orElseGet(() -> "Y"); // Returns X
    
    Try.of(() -> null).get(); // Throws NoSuchElementException
    
    Try.of(() -> null).orElse("Y"); // Returns Y
    
    Try.of(() -> null).orElseGet(() -> "Y"); // Returns Y
    
    Try.of(() -> { throw new IOException(); }).get(); // Throws NoSuchElementException
    
    Try.of(() -> { throw new IOException(); }).orElse("Y"); // Returns Y

    Try.of(() -> { throw new IOException(); }).orElseGet(() -> "Y"); // Returns Y

Use orThrow() to get the value or re-throw the exception: 

    Try.of(() -> "X").orThrow(); // Returns X
    
    Try.of(() -> "X").orThrow("Y"); // Returns X
    
    Try.of(() -> null).orThrow(); // Throws NoSuchElementException
    
    Try.of(() -> null).orThrow("Y"); // Returns Y
    
    Try.of(() -> { throw new IOException(); }).orThrow(); // Throws IOException
    
    Try.of(() -> { throw new IOException(); }).orThrow("Y"); // Throws IOException

## Checking value

Use isPresent(), ifPresent(), ifPresentOrThrow() to conditionally execute code: 

    Try.of(() -> "X").isPresent(); // Returns true
    
    Try.of(() -> null).isPresent(); // Returns false
    
    Try.of(() -> { throw new Exception(); }).isPresent(); // Returns false
    
    Try.of(() -> "X").ifPresent(consumer); // Invokes the consumer
    
    Try.of(() -> null).ifPresent(consumer); // Doesn't invoke the consumer
    
    Try.of(() -> { throw new Exception(); }).ifPresent(consumer); // Doesn't invoke the consumer
    
    Try.of(() -> "X").ifPresentOrThrow(consumer); // Invokes the consumer
    
    Try.of(() -> null).ifPresentOrThrow(consumer); // Doesn't invoke the consumer
    
    Try.of(() -> { throw new Exception(); }).ifPresentOrThrow(consumer); // Throws Exception

## Checking state

Use isSuccess(), isFailure(), ifSuccess() or ifFailure() to conditionally execute code: 

    Try.of(() -> "X").isSuccess(); // Returns true
    
    Try.of(() -> null).isSuccess(); // Returns true
    
    Try.of(() -> { throw new Exception(); }).isSuccess(); // Returns false
    
    Try.of(() -> "X").isFailure(); // Returns false
    
    Try.of(() -> null).isFailure(); // Returns false
    
    Try.of(() -> { throw new Exception(); }).isFailure(); // Returns true
    
    Try.of(() -> "X").ifSuccess(consumer); // Invokes the consumer
    
    Try.of(() -> null).ifSuccess(consumer); // Invokes the consumer
    
    Try.of(() -> { throw new Exception(); }).ifSuccess(consumer); // Doesn't invoke the consumer 
       
    Try.of(() -> "X").ifFailure(consumer); // Doesn't invoke the consumer
    
    Try.of(() -> null).ifFailure(consumer); // Doesn't invoke the consumer
    
    Try.of(() -> { throw new Exception(); }).ifFailure(consumer); // Invokes the consumer    

## Receiving events

Use onSuccess() and onFailure() to receive events:
  
    Try.of(() -> "X").onSuccess(consumer).get(); // Invokes the consumer
    
    Try.of(() -> null).onSuccess(consumer).orElse(null); // Invokes the consumer
    
    Try.of(() -> { throw new Exception(); }).onSuccess(consumer).orElse(null); // Doesn't invoke the consumer
    
    Try.of(() -> "X").onFailure(consumer).get(); // Doesn't invoke the consumer
    
    Try.of(() -> null).onFailure(consumer).orElse(null); // Doesn't invoke the consumer
    
    Try.of(() -> { throw new Exception(); }).onFailure(consumer).orElse(null); // Invokes the consumer

## Mapping and filtering value

Use map() or flatMap() to transform the value:

    Try.of(() -> "X").map(x -> x.toLowerCase()).ifPresent(System.out::println); // Prints x
    
    Try.of(() -> "X").map(x -> null).isPresent(); // Returns false
    
    Try.of(() -> null).map(x -> "X").isPresent(); // Returns false
    
    Try.of(() -> { throw new Exception(); }).map(x -> "X").isPresent(); // Returns false
    
    Try.of(() -> "X").flatMap(x -> Try.of(() -> "Y")).ifPresent(System.out::println); // Prints Y
    
    Try.of(() -> "X").flatMap(x -> Try.of(() -> { throw new Exception(x); })).isFailure(); // Returns true
    
Use filter() to filter the value:

    Try.of(() -> "X").filter(v -> "X".equals(v)).isPresent(); // Returns true
    
    Try.of(() -> "X").filter(v -> "Y".equals(v)).isPresent(); // Returns false
    
    Try.of(() -> null).filter(v -> "Y".equals(v)).isPresent(); // Returns false
    
    Try.of(() -> { throw new Exception(); }).filter(v -> true).isPresent(); // Returns false

## Remapping exception

Use mapper() to change type of exception:

    Try.of(() -> { throw new Exception(); }).mapper(mapper()).ifFailure(e -> handleException(e));
    
    private static void handleException(IOException e) {
        System.out.println("IOException: " + e.getMessage());
    }

    private static Function<Exception, IOException> mapper() {
       return e -> (e instanceof IOException) ? (IOException)e : new IOException("IO Error", e);
    }

## Executing consecutive or alternative operations 

Use or() to provide an alternative callable to invoke if failure:

    Try.of(() -> "X").or(() -> "Y").get(); // Returns X  
      
    Try.of(() -> { throw new Exception(); }).or(() -> "Y").get(); // Returns Y    

    Try.of(() -> { throw new Exception(); }).or(() -> "Y").isFailure(); // Returns false    

Use and() to provide an consecutive callable to invoke if success:

    Try.of(() -> "X").and(() -> "Y").get(); // Returns Y  
      
    Try.of(() -> { throw new Exception(); }).and(() -> "Y").orElse(null); // Returns null    

    Try.of(() -> { throw new Exception(); }).and(() -> "Y").isFailure(); // Returns true    

## Complete example

Given the program:

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

The output will be:

    x
    X
    Exception: Error
    IOException: IO Error
    Y
