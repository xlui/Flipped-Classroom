package io.flippedclassroom.server.utils;

import io.flippedclassroom.server.entity.Course;

/**
 * 生成课程唯一代码
 */
public class CourseCodeUtil {
	public static void generateCode(Course course) {
		course.setCode("xust" + course.hashCode());
	}
}
