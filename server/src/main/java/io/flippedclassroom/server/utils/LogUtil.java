package io.flippedclassroom.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局日志
 */
public class LogUtil {
	private static Logger logger = null;

	public static Logger getLogger() {
		if (logger == null) {
			logger = LoggerFactory.getLogger("FlippedClassroom");
		}
		return logger;
	}
}
