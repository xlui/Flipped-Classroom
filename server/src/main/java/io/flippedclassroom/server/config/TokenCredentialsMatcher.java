package io.flippedclassroom.server.config;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.utils.JWTUtil;
import io.flippedclassroom.server.utils.LogUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义的 Token 校验类，通过验证 Token 实现校验
 */
public class TokenCredentialsMatcher extends SimpleCredentialsMatcher {
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		Logger logger = LogUtil.getLogger();

		logger.info("进入自定义 Token 校验！");
		String principal = (String) token.getPrincipal();
		String credentials = (String) token.getCredentials();

		// Redis Check First
		if (!redisService.get(principal).equals(credentials)) {
			logger.info("Redis 校验失败！！！");
			return false;
		}

		logger.info("Token 中的信息：\n" + principal + ", " + credentials);
		User user = userService.findUserByUsername(principal);

		return JWTUtil.verify(credentials, user.getUsername(), user.getPassword());
	}
}
