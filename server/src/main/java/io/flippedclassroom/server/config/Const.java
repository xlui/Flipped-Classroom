package io.flippedclassroom.server.config;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Const {
	// version
	public static final String swaggerVersion = "v0.5.1";
	public static final String version = "v0.0.1";

	// 返回数据的 Status
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";

	// Password Encrypt
	public static final String algorithm = "MD5";
	public static final int iteration = 1024;

	// 即时通讯的订阅点
	public static final String webSocketEndpoint = "/im";
	public static final String broadcast = "/b";
	public static final String group = "/g";
	public static final String user = "/user";

	// token 有效期，当前设置为十天
	public static long expire() {
		return System.currentTimeMillis() + Duration.ofDays(10).toMillis();
	}

	// 格式化的当前时间
	public static String currentTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	// 上传文件大小
	public static final int size1M = 1024 * 1024;    // 1M

	public static final String defaultAvatarLink = "https://www.gravatar.com/avatar/MD5?d=identicon";
	public static final String coursePictureLink = "https://fc.xd.style/course/COURSEID/picture";

	// 上传文件位置
	private static final String basePosition = "/var/www/uploads/";
	public static final String avatarPosition = basePosition + "avatar/";
	public static final String coursePreviewPosition = basePosition + "course/preview/";
	public static final String courseEDataPosition = basePosition + "course/edata/";
	public static final String coursePicture = basePosition + "course/picture/";
}
