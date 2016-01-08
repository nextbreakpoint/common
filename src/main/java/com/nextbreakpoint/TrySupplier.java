package com.nextbreakpoint;

@FunctionalInterface
public interface TrySupplier<R> {
    public R supply() throws Exception;
}