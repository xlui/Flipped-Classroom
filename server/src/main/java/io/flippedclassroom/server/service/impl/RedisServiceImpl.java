package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void save(String username, String token) {
		stringRedisTemplate.opsForValue().set(username, token);
	}

	@Override
	public String get(String username) {
		return stringRedisTemplate.opsForValue().get(username);
	}

	@Override
	public void delete(String name) {
		stringRedisTemplate.delete(name);
	}
}
