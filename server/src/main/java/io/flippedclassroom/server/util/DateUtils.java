package io.flippedclassroom.server.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	// token 有效期，当前设置为十天
	public static long expire() {
		return System.currentTimeMillis() + Duration.ofDays(10).toMillis();
	}

	// 格式化的当前时间
	public static String currentTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
