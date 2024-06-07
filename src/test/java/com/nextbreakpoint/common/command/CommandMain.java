package com.nextbreakpoint.common.command;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandMain {
	private static final Logger LOG = Logger.getLogger(CommandMain.class.getName());

	public static void main(String[] args) {
		Command.of(() -> "Example 0: Hello World !")
				.map(String::toUpperCase)
				.execute()
				.observe()
				.onSuccess(CommandMain::printValue)
				.onFailure(CommandMain::printException)
				.get();

		Command.value("Example 1: Hello World !")
				.flatMap(x -> Command.<String>error(new Exception("Example 1: Error")))
				.execute()
				.observe()
				.onSuccess(CommandMain::printValue)
				.onFailure(CommandMain::printException)
				.get();

		Command.<String>error(new Exception("Example 2: Error"))
				.execute()
				.observe()
				.onSuccess(CommandMain::printValue)
				.onFailure(CommandMain::printException)
				.get();
	}

	private static void printException(Exception error) {
		LOG.log(Level.INFO, "Some error occurred", error);
	}

	private static void printValue(String value) {
		LOG.log(Level.INFO, "Value: " + value);
	}
}
