package io.flippedclassroom.server.filter;

import io.flippedclassroom.server.config.Const;
import io.flippedclassroom.server.config.token.TokenToken;
import io.flippedclassroom.server.entity.response.JsonResponse;
import io.flippedclassroom.server.util.JsonUtils;
import io.flippedclassroom.server.util.LogUtils;
import io.flippedclassroom.server.util.TokenUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token 校验的 Filter，用于 ShiroConfiguration 的 ShiroFilterFactoryBean，
 * 添加到 `filterChainDefinitionMap` 中的 value 为 "jwt" 的端点在进入 Realm 校验之前需要先经过 TokenFilter
 * 其中值为 “jwt” 并不是强制，是自己定义的。详细参考 `ShiroFilterFactoryBean`
 */
public class TokenFilter extends BasicHttpAuthenticationFilter {
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Access-control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Methods", httpRequest.getMethod());
		httpResponse.setHeader("Access-Control-Allow-Headers", httpRequest.getHeader("Access-Control-Request-Headers"));
		httpResponse.setStatus(HttpStatus.OK.value());
		return super.preHandle(request, response);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		Logger logger = LogUtils.getInstance();
		logger.info("进入 Token 验证的 Filter");

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String authorization = httpServletRequest.getHeader("Authorization");

		if (authorization != null) {
			logger.info("成功在 HTTP 头部发现 Authorization，进入 Token 验证...");
			logger.info("记录 Authorization: " + authorization);
			try {
				String username = TokenUtils.getUsername(authorization);
				TokenToken tokenToken = new TokenToken(username, authorization);
				getSubject(request, response).login(tokenToken);
				return true;
			} catch (AuthenticationException e) {
				return tokenAuthFailed(httpServletResponse);
			}
		} else {
			return tokenAuthFailed(httpServletResponse);
		}
	}

	private boolean tokenAuthFailed(HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		JsonResponse jsonResponse = new JsonResponse(Const.FAILED, "need token auth");
		response.getOutputStream().print(JsonUtils.getInstance().writeValueAsString(jsonResponse));
		return false;
	}
}
