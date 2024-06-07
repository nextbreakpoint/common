package com.nextbreakpoint.common.either;

import java.util.logging.Level;
import java.util.logging.Logger;

class EitherMain {
    private static final Logger LOG = Logger.getLogger(EitherMain.class.getName());

    public static void main(String[] args) {
        Either.success("Example 0: Hello World !")
                .map(String::toUpperCase)
                .observe()
                .onSuccess(EitherMain::printValue)
                .onFailure(EitherMain::printException)
                .get();

        Either.success("Example 1: Hello World !")
                .flatMap(x -> Either.<String>failure(new Exception("Example 1: Error")))
                .observe()
                .onSuccess(EitherMain::printValue)
                .onFailure(EitherMain::printException)
                .get();

        Either.<String>failure(new Exception("Example 2: Error"))
                .observe()
                .onSuccess(EitherMain::printValue)
                .onFailure(EitherMain::printException)
                .get();

        Either.<String>failure(new Exception("Example 3: Error"))
                .or(() -> Either.success("Example 3: Hello World !"))
                .observe()
                .onSuccess(EitherMain::printValue)
                .onFailure(EitherMain::printException)
                .get();

        printValue(Either.<String>success(null)
                .observe()
                .onSuccess(EitherMain::printValue)
                .onFailure(EitherMain::printException)
                .get()
                .orElseGet(() -> "Example 4: Hello World !"));
    }

    private static void printException(Exception error) {
        LOG.log(Level.INFO, "Some error occurred", error);
    }

    private static void printValue(String value) {
        LOG.log(Level.INFO, "Value: " + value);
    }
}
