package io.flippedclassroom.server.util;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.exception.AssertException;
import io.flippedclassroom.server.exception.InputException;
import io.flippedclassroom.server.exception.PositionInvalidException;

import java.io.File;

/**
 * 通用断言
 */
public class AssertUtils {
	public static void assertNotNull(String... args) throws AssertException {
		for (String arg : args) {
			if (arg == null)
				throw new AssertException();
		}
	}

	public static void assertUsernamePasswordNotNull(User user) throws InputException {
		if (user.getUsername() == null || user.getPassword() == null) {
			throw new InputException("username or password invalid!");
		}
	}

	public static void assertPositionValid(String position) throws PositionInvalidException {
		if (position == null || position.length() == 0) {
			throw new PositionInvalidException();
		}
		File file = new File(position);
		if (!file.exists()) {
			throw new PositionInvalidException();
		}
	}
}
