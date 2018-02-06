package io.flippedclassroom.server.annotation.resolver;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.LogUtils;
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
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
	@Autowired
	private UserService userService;

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.getParameterType().isAssignableFrom(User.class) &&
				methodParameter.hasParameterAnnotation(CurrentUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		LogUtils.getLogger().info("注入当前登录用户");
		AuthenticationToken token = (AuthenticationToken) SecurityUtils.getSubject().getPrincipal();
		String username = (String) token.getPrincipal();
		if (username != null) {
			return userService.findUserByUsername(username);
		}
		throw new MissingServletRequestPartException("Injection Failed!");
	}
}
