package io.flippedclassroom.server.config;

import io.flippedclassroom.server.entity.User;
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

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		Logger logger = LogUtil.getLogger();

		logger.info("进入自定义 Token 校验！");
		TokenToken tokenToken = (TokenToken) token;
		logger.info("Token 中的信息：\n" + tokenToken.getPrincipal() + ", " + tokenToken.getCredentials());
		User user = userService.findUserByUsername((String) tokenToken.getPrincipal());

		return JWTUtil.verify((String) tokenToken.getCredentials(), user.getUsername(), user.getPassword());
	}
}
