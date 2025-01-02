package org.pokesplash.gts.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.gts.Gts;

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
		logger = LogManager.getLogger(Gts.MOD_ID);
	}

	/**
	 * Info log method.
	 * @param message The message to log.
	 */
	public void info(String message) {
		logger.info(message);
	}

	/**
	 * Error log method.
	 * @param message The message to log.
	 */
	public void error(String message) {
		logger.error(message);
	}

	/**
	 * Fatal log method.
	 * @param message The message to log.
	 */
	public void fatal(String message) {
		logger.fatal(message);
	}

}
