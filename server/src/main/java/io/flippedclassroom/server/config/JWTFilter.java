package io.flippedclassroom.server.config;

import io.flippedclassroom.server.utils.JWTUtil;
import io.flippedclassroom.server.utils.LogUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Token 校验的 Filter，用于 ShiroConfiguration 的 ShiroFilterFactoryBean，
 * 添加到 `filterChainDefinitionMap` 中的 value 为 "jwt" 的端点在进入 Realm 校验之前需要先经过 JWTFilter
 * 其中值为 “jwt” 并不是强制，是自己定义的。详细参考 `ShiroFilterFactoryBean`
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
		Logger logger = LogUtil.getLogger();
		logger.info("进入 Token 验证的 Filter");

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String authorization = httpServletRequest.getHeader("Authorization");
		if (authorization != null) {
			logger.info("成功在 HTTP 头部发现 Authorization，进入 Token 验证...");
			logger.info("记录 Authorization: " + authorization);
			try {
				String username = JWTUtil.getUsername(authorization);
				TokenToken tokenToken = new TokenToken(username, authorization);
				getSubject(request, response).login(tokenToken);
				return true;
			} catch (AuthenticationException e) {
				throw new AuthenticationException("Token 认证失败！");
//				todo: Shiro 的异常并不算在 ControllerAdvice 中，所以如果 token 认证失败，这里会抛出异常。给用户的显示就是 500 异常
			}
		} else {
			return false;
		}
	}
}
