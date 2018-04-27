package io.flippedclassroom.server.config;

public class Const {
	// version
	public static final String swaggerVersion = "v0.5.8";
	public static final String version = "v0.0.1";

	// 返回数据的 Status
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";

	// 身份类型
	public static final String Student = "student";
	public static final String Teacher = "teacher";

	// Password Encrypt
	public static final String algorithm = "MD5";
	public static final int iteration = 1024;

	// 即时通讯的订阅点
	public static final String webSocketEndpoint = "/im";
	public static final String broadcast = "/b";
	public static final String group = "/g";
	public static final String user = "/user";

	// 上传文件大小
	public static final int size1M = 1024 * 1024;    // 1M

	public static final String defaultAvatarLink = "https://www.gravatar.com/avatar/MD5?d=identicon";
	public static final String showAvatar = "https://api.fc.xd.style/avatar";
	public static final String coursePictureLink = "https://fc.xd.style/course/COURSEID/picture";

	// 上传文件位置
	private static final String basePosition = "/var/www/uploads/";
	public static final String avatarPosition = basePosition + "avatar/";
	public static final String coursePreviewPosition = basePosition + "course/preview/";
	public static final String courseEDataPosition = basePosition + "course/edata/";
	public static final String coursePicture = basePosition + "course/picture/";
}
