package io.flippedclassroom.server.config;

import java.time.Duration;

public class Constant {
	// version
	public static final String version = "v0.0.1";

	// Password Encrypt
	public static final String algorithm = "MD5";
	public static final int iteration = 1024;

	// 返回数据的 Status
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	public static final String ROLE = "ROLE";

	// token 有效期
	public static final long expire = System.currentTimeMillis() + Duration.ofDays(1).toMillis();
}
