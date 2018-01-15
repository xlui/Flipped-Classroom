package io.flippedclassroom.server.advice;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 控制器建言，全局异常处理
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {
	@ExceptionHandler(value = AuthenticationException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Map handle401() {
		Map<String, String> map = new HashMap<>();
		map.put("status", HttpStatus.FORBIDDEN.toString());
		map.put("message", "Oops! 身份认证失败！");
		return map;
	}
}
