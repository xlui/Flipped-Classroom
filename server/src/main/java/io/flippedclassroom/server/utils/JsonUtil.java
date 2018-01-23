package io.flippedclassroom.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用于手动将对象序列化为 JSON，保持 controller 内外返回的数据一致
 * 原因是 controller 外不能使用注解 @ResponseBody
 */
public class JsonUtil {
	private static ObjectMapper objectMapper = null;

	public static ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		return objectMapper;
	}
}
