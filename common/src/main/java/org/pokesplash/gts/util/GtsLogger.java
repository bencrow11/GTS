package org.pokesplash.gts.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class for logging.
 */
public class GtsLogger {
	private Logger logger; // Log for the console.

	// Enums used for the log file.
	private enum Level {
		INFO,
		ERROR,
		FATAL
	}

	// Constructor that creates the logger.
	public GtsLogger() {
		logger = LogManager.getLogger();
	}

	/**
	 * Info log method.
	 * @param message The message to log.
	 */
	public void info(String message) {
		logger.info(message);
		write(Level.INFO, message);
	}

	/**
	 * Error log method.
	 * @param message The message to log.
	 */
	public void error(String message) {
		logger.error(message);
		write(Level.ERROR, message);
	}

	/**
	 * Fatal log method.
	 * @param message The message to log.
	 */
	public void fatal(String message) {
		logger.fatal(message);
		write(Level.FATAL, message);
	}

	/**
	 * Write method to save the logs to file.
	 * @param level The level that the log is (INFO, ERROR or FATAL).
	 * @param message The message to log.
	 */
	private void write(Level level, String message) {

		String output = "[" + level + "]: " + message;

		Utils.writeFileAsync("/config/gts/", "logs.txt", output, true);
	}
}
