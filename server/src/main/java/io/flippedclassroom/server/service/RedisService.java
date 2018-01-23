package io.flippedclassroom.server.service;

/**
 * 利用 Redis 缓存 Token
 */
public interface RedisService {
	void save(String username, String token);

	String get(String username);
}
