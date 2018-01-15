package io.flippedclassroom.server.utils;

import io.flippedclassroom.server.config.Constant;
import io.flippedclassroom.server.entity.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.Date;

public class EncryptUtil {
	/**
	 * 生成随机盐，并加密密码
	 * @param user 需要加密密码的用户
	 */
	public static void encrypt(User user) {
		SecureRandomNumberGenerator numberGenerator = new SecureRandomNumberGenerator();
		String salt = user.getUsername() + numberGenerator.nextBytes(12).toHex() + new Date().getTime();
		user.setSalt(salt);
		user.setPassword((new SimpleHash(Constant.algorithm, user.getPassword(), ByteSource.Util.bytes(salt), Constant.iteration)).toString());
	}
}
