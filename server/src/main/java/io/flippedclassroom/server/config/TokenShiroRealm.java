package io.flippedclassroom.server.config;

import io.flippedclassroom.server.utils.LogUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Shiro Realm 1: Token 登录验证
 */
public class TokenShiroRealm extends AuthorizingRealm {
	/**
	 * 通过重写 supports 方法来避免重复校验
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof TokenToken;
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		return null;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		LogUtil.getLogger().info("开始 token 认证");
		TokenToken tokenToken = (TokenToken) authenticationToken;
		LogUtil.getLogger().info("从输入得到的用户名：" + tokenToken.getPrincipal());

		return new SimpleAuthenticationInfo(
				tokenToken,
				tokenToken.getCredentials(),
				getName()
		);
	}
}
