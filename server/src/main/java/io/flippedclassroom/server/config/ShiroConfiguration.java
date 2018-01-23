package io.flippedclassroom.server.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置
 * 参考 https://xlui.me/t/spring-boot-shiro/
 */
@Configuration
public class ShiroConfiguration {
	/**
	 * 为基于用户名密码的验证注入加密方式
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName(Constant.algorithm);
		credentialsMatcher.setHashIterations(Constant.iteration);
		return credentialsMatcher;
	}

	/**
	 * 为基于 Token 的验证注入验证方式
	 */
	@Bean
	public TokenCredentialsMatcher tokenCredentialsMatcher() {
		return new TokenCredentialsMatcher();
	}

	/**
	 * 多 Realm 情况下配置仅通过一个 Realm 的验证即可
	 */
	@Bean
	public ModularRealmAuthenticator modularRealmAuthenticator() {
		ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
		modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
		return modularRealmAuthenticator;
	}

	/**
	 * 基于 Token 的 Realm
	 * 注入了自定义的验证类
	 */
	@Bean
	public TokenShiroRealm tokenShiroRealm() {
		TokenShiroRealm tokenShiroRealm = new TokenShiroRealm();
		tokenShiroRealm.setCredentialsMatcher(tokenCredentialsMatcher());
		return tokenShiroRealm;
	}

	/**
	 * 基于 用户名密码 的 Realm
	 * 注入了加密方式
	 */
	@Bean
	public PasswordShiroRealm passwordShiroRealm() {
		PasswordShiroRealm passwordShiroRealm = new PasswordShiroRealm();
		passwordShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return passwordShiroRealm;
	}

	/**
	 * Shiro 配置的核心
	 */
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// ModularRealmAuthenticator 的位置要放在 Realms 前，否则会报找不到 Realm 异常
		securityManager.setAuthenticator(modularRealmAuthenticator());
		securityManager.setRealms(Arrays.asList(tokenShiroRealm(), passwordShiroRealm()));

		// REST 模式下关闭 Shiro 自带的 Session
		DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		defaultSubjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(defaultSubjectDAO);

		return securityManager;
	}

	/**
	 * URL 认证授权规则
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		Map<String, Filter> filterMap = new HashMap<>();
		filterMap.put("jwt", new JWTFilter());
		shiroFilterFactoryBean.setFilters(filterMap);

		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		filterChainDefinitionMap.put("/init", "anon");  // 用于数据库的初始化
		filterChainDefinitionMap.put("/hello", "jwt");  // 测试 jwt filter
		filterChainDefinitionMap.put("/**", "authc");

		shiroFilterFactoryBean.setLoginUrl("/login");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}
}
