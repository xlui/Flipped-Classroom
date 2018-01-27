package io.flippedclassroom.server.annotation.resolver;

import io.flippedclassroom.server.annotation.CurrentRole;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.utils.LogUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Component
public class CurrentRoleMethodArgumentResolver implements HandlerMethodArgumentResolver {
	@Autowired
	private UserService userService;

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.getParameterType().isAssignableFrom(String.class) && methodParameter.hasParameterAnnotation(CurrentRole.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		LogUtil.getLogger().info("注入当前用户的 Role");
		AuthenticationToken token = (AuthenticationToken) SecurityUtils.getSubject().getPrincipal();
		String username = (String) token.getPrincipal();
		if (username != null) {
			return userService.findUserByUsername(username).getRole().getRole();
		}
		throw new MissingServletRequestPartException("Injection Failed!");
	}
}
