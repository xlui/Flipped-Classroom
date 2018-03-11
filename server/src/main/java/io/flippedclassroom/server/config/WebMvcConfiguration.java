package io.flippedclassroom.server.config;

import io.flippedclassroom.server.annotation.resolver.CurrentRoleMethodArgumentResolver;
import io.flippedclassroom.server.annotation.resolver.CurrentUserMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
	// 注入自定义注解
	@Bean
	public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
		return new CurrentUserMethodArgumentResolver();
	}

	// 注入自定义注解
	@Bean
	public CurrentRoleMethodArgumentResolver currentRoleMethodArgumentResolver() {
		return new CurrentRoleMethodArgumentResolver();
	}

	// 文件上传相关
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(Const.size1M * 2);
		return multipartResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(currentUserMethodArgumentResolver());
		argumentResolvers.add(currentRoleMethodArgumentResolver());
	}
}
