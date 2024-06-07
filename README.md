# Common 3.0.0

Common is a library which implement useful types for writing code in an elegant and concise style.

## License

This library is distributed under the terms of BSD 3-Clause License.

    Copyright (c) 2016-2024, Andrea Medeghini
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this
      list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

    * Neither the name of this library nor the names of its
      contributors may be used to endorse or promote products derived from
      this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
    FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
    DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
    CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
    OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

## Binary distribution

Common library is available in Maven Central Repository and GitHub.

If you are using Maven, add the following dependency in your POM:

    <dependency>
        <groupId>com.nextbreakpoint</groupId>
        <artifactId>com.nextbreakpoint.common</artifactId>
        <version>3.0.0</version>
    </dependency>

## Documentation

The primary goal of this library is to provide the types for writing code in an elegant and concise style.
Also, the library aims to provide the building blocks for creating composable and reusable code.   

For instance, a typical fragment of code for handling exceptions in Java looks like:

    try {
        doSomething();
    } catch (SomeException e) {
        handleException(e);
    }

Using Command and Either provided in the library we can rewrite the same code in a more elegant style:

    Command.of(() -> doSomething())
        .execute()
        .observe()
        .onFailure(e -> handleException(e))
        .get();

### Either class

Either implements a fluent interface for composing operations and handling errors.

Either can be created from a value or an exception:

    Either.success(value)
    Either.failure(exception)

Use get(), orElse(), orElseGet() to get the value:

    Either.success(value).get()
    Either.success(value).orElse(otherValue)
    Either.success(value).orElseGet(() -> otherValue)

Use orThrow() to rethrow the exception if present:

    Either.failure(exception).orThrow()
    Either.failure(exception).orThrow(e -> new SomeException(e))

Use or() to produce an alternative result:

    // the composed either will ignore the failure and return the alternative value
    Either.failure(exception).or(Either.success(value))

Use map() and flatMap() to compose transformations:

    Either.success(value).map(value -> transform(value))
    Either.success(value).flatMap(value -> Either.success(transform(value)))

Use exception() to get the exception if present:

    // always null if either is success
    Either.success(value).exception()
    // always not null if either is failure
    Either.failure(exception).exception()

Use isSuccess() and isFailure() to check the status:

    // always returns true if either is success
    Either.success(value).isSuccess()
    // always returns true if either is failure
    Either.failure(exception).isFailure()

Use observe() to observe the status:

    Either.success(value)
        .observe()
        .onSuccess(value -> handleValue(value))
        .onFailure(error -> handleError(error))
        .get()

Use optional() to create an Optional:

    Either.success(value).optional()

Use stream() to create a Stream:

    Either.success(value).stream()

### Command class

Command implements a fluent interface for composing operations and execute them as single command.

Command can be created from a value, an exception, an instance of Either, or an instance of Callable:

    Command.value(value)
    Command.error(exception)
    Command.of(Either.success(value))
    Command.of(() -> doSomething())

Use execute() to execute the composed operation:

    // it will execute the command and return the result as instance of Either
    Command.of(() -> doSomething()).execute();

Use map() and flatMap() to compose operations:

    Command.of(() -> doSomething()).map(value -> processValue(value));
    Command.of(() -> doSomething()).flatMap(value -> Command.of(processValue(value)));

