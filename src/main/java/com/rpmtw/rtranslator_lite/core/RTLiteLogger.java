package com.rpmtw.rtranslator_lite.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RTLiteLogger {
    private static final Logger logger = LogManager.getLogger();

    private static String message(String message) {
        return String.format("[RTLite] %s", message);
    }

    public static void info(String message) {
        logger.info(message(message));
    }

    public static void error(String message) {
        logger.error(message(message));
    }

    public static void error(String message, Throwable t) {
        logger.error(message(message), t);
    }

    public static void warn(String message) {
        logger.warn(message(message));
    }
}
