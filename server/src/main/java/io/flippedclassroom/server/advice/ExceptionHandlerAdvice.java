package io.flippedclassroom.server.advice;

import io.flippedclassroom.server.config.Constant;
import io.flippedclassroom.server.entity.JsonResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 控制器建言，全局异常处理
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {
	@ExceptionHandler(value = AuthenticationException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public JsonResponse handle403() {
		return new JsonResponse(Constant.FAILED, "Oops! 身份认证失败！");
	}

	@ExceptionHandler(value = AuthorizationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public JsonResponse handle401() {
		return new JsonResponse(Constant.FAILED, "Oops! 权限鉴定失败！");
	}
}
