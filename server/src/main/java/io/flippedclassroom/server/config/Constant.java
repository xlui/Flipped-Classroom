package io.flippedclassroom.server.config;

import java.time.Duration;

public class Constant {
	// version
	public static final String swaggerVersion = "v0.3.1";
	public static final String version = "v0.0.1";

	// Password Encrypt
	public static final String algorithm = "MD5";
	public static final int iteration = 1024;

	// 返回数据的 Status
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";

	// token 有效期，当前设置为一天
	public static final long expire = System.currentTimeMillis() + Duration.ofDays(1).toMillis();

	// 上传文件大小
	public static final int size1M = 1024 * 1024;    // 1M

	// 上传文件位置
	public static final String defaultAvatarLink = "https://www.gravatar.com/avatar/MD5?d=identicon";
	public static final String avatarPosition = "/home/liuqi/uploads/avatar/";
	public static final String coursePreviewPosition = "/home/liuqi/uploads/course/preview/";
	public static final String courseEDataPosition = "/home/liuqi/uploads/course/edata/";
}
