package io.flippedclassroom.server.config;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.JWTUtils;
import io.flippedclassroom.server.util.LogUtils;
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
		Logger logger = LogUtils.getInstance();

		logger.info("进入自定义 Token 校验！");
		String principal = (String) token.getPrincipal();
		String credentials = (String) token.getCredentials();
		String redisCheck = redisService.get(principal);

		// Redis Check First
		if (!redisCheck.equals(credentials)) {
			logger.warn("Redis 校验失败！！！");
			logger.info("Redis 中得到的 Token：" + redisCheck);
			logger.info("进行校验的 Token：" + credentials);
			return false;
		}

		logger.info("Token 中的信息：\n" + principal + ", " + credentials);
		User user = userService.findUserByUsername(principal);

		boolean result = JWTUtils.verify(credentials, user.getUsername(), user.getPassword());
		logger.info("Token 校验结果：" + result);
		return result;
	}
}
