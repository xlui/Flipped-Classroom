package io.flippedclassroom.server.config;

import java.time.Duration;

public class Const {
	// version
	public static final String swaggerVersion = "v0.4.6";
	public static final String version = "v0.0.1";

	// Password Encrypt
	public static final String algorithm = "MD5";
	public static final int iteration = 1024;

	// 返回数据的 Status
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";

	// token 有效期，当前设置为十天
	public static long expire() {
		return System.currentTimeMillis() + Duration.ofDays(10).toMillis();
	}

	// 上传文件大小
	public static final int size1M = 1024 * 1024;    // 1M

	public static final String defaultAvatarLink = "https://www.gravatar.com/avatar/MD5?d=identicon";
	public static final String coursePictureLink = "https://fc.xd.style/course/COURSEID/picture";

	// 上传文件位置
	public static final String avatarPosition = "/var/www/uploads/avatar/";
	public static final String coursePreviewPosition = "/var/www/uploads/course/preview/";
	public static final String courseEDataPosition = "/var/www/uploads/course/edata/";
	public static final String coursePicture = "/var/www/uploads/course/picture/";
}
