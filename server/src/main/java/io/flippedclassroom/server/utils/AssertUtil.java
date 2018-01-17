package io.flippedclassroom.server.utils;

import io.flippedclassroom.server.entity.User;
import org.apache.shiro.authc.AuthenticationException;

/**
 * 通用断言
 */
public class AssertUtil {
	/**
	 * 断言用户名、密码非空
	 *
	 * @param user 用户类
	 */
	public static void assertUsernamePasswordNotNull(User user) {
		if (user.getUsername() == null || user.getPassword() == null) {
			throw new AuthenticationException("username or password invalid!");
		}
	}
}
