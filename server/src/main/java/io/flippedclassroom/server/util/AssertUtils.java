package io.flippedclassroom.server.util;

import io.flippedclassroom.server.exception.AssertException;

import java.io.File;
import java.util.function.Supplier;

/**
 * 通用断言
 */
public class AssertUtils {
	public static void assertNull(Object object) throws AssertException {
		assertNull(object, "Object 应该为空！");
	}

	public static void assertNull(Object object, String msg) throws AssertException {
		if (object != null)
			throw new AssertException(msg);
	}

	public static <X extends Throwable> void assertNullElseThrow(Object object, Supplier<? extends X> exceptionSupplier) throws X {
		if (object != null)
			throw exceptionSupplier.get();
	}

	public static void assertNotNull(Object... objects) throws AssertException {
		for (Object object : objects) {
			assertNotNull(object, "所有输入不应为空！");
		}
	}

	public static void assertNotNull(Throwable throwable, Object... objects) throws Throwable {
		for (Object object : objects) {
			assertNotNUllElseThrow(object, () -> throwable);
		}
	}

	public static void assertNotNull(Object object) throws AssertException {
		assertNotNull(object, "Object 不应为空！");
	}

	public static void assertNotNull(Object object, String msg) throws AssertException {
		if (object == null)
			throw new AssertException(msg);
	}

	public static <X extends Throwable> void assertNotNUllElseThrow(Object object, Supplier<? extends X> exceptionSupplier) throws X {
		if (object == null)
			throw exceptionSupplier.get();
	}

	public static void assertEquals(Object object1, Object object2) throws AssertException {
		assertEquals(object1, object2, object1 + " and " + object2 + " must be equal!");
	}

	public static void assertEquals(Object object1, Object object2, String msg) throws AssertException {
		if (!object1.equals(object2))
			throw new AssertException(msg);
	}

	public static <X extends Throwable> void assertEqualsElseThrow(Object object1, Object object2, Supplier<? extends X> exceptionSupplier) throws X {
		assertNotNUllElseThrow(object1, exceptionSupplier);
		assertNotNUllElseThrow(object2, exceptionSupplier);
		if (!object1.equals(object2))
			throw exceptionSupplier.get();
	}

	public static void assertTrue(boolean exp) throws AssertException {
		assertTrue(exp, "expression must be True!");
	}

	public static void assertTrue(boolean exp, String msg) throws AssertException {
		if (!exp)
			throw new AssertException(msg);
	}

	public static <X extends Throwable> void assertTrueElseThrow(boolean exp, Supplier<? extends X> exceptionSupplier) throws X {
		if (!exp)
			throw exceptionSupplier.get();
	}

	public static void assertFalse(boolean exp) throws AssertException {
		assertFalse(exp, "expression must be False!");
	}

	public static void assertFalse(boolean exp, String msg) throws AssertException {
		if (exp)
			throw new AssertException(msg);
	}

	public static <X extends Throwable> void assertFalseElseThrow(boolean exp, Supplier<? extends X> exceptionSupplier) throws X {
		if (exp)
			throw exceptionSupplier.get();
	}

	public static void assertPositionValid(String position) throws AssertException {
		if (position == null || position.length() == 0) {
			throw new AssertException();
		}
		File file = new File(position);
		if (!file.exists()) {
			throw new AssertException();
		}
	}

	public static <X extends Throwable> void assertPositionValidElseThrow(String position, Supplier<? extends X> exceptionSupplier) throws X {
		if (position == null || position.length() == 0) {
			throw exceptionSupplier.get();
		}
		File file = new File(position);
		if (!file.exists()) {
			throw exceptionSupplier.get();
		}
	}
}
