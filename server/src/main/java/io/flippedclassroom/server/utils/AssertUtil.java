package io.flippedclassroom.server.utils;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.exception.InputException;

/**
 * 通用断言
 */
public class AssertUtil {
	/**
	 * 断言用户名、密码非空
	 *
	 * @param user 用户类
	 */
	public static void assertUsernamePasswordNotNull(User user) throws InputException {
		if (user.getUsername() == null || user.getPassword() == null) {
			throw new InputException("username or password invalid!");
		}
	}
}
