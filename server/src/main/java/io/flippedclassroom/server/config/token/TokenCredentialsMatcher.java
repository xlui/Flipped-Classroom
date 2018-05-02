package io.flippedclassroom.server.config.token;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.TokenUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
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
		String principal = (String) token.getPrincipal();
		String credentials = (String) token.getCredentials();
		String redisCheck = redisService.get(principal);

		// Redis Check First
		if (!redisCheck.equals(credentials)) {
			return false;
		}

		User user = userService.findUserByUsername(principal);

        return TokenUtils.verify(credentials, user.getUsername(), user.getPassword());
	}
}
