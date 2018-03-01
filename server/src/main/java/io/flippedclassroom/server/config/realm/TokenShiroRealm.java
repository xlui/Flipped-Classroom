package io.flippedclassroom.server.config.realm;

import io.flippedclassroom.server.config.token.TokenToken;
import io.flippedclassroom.server.entity.Permission;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.LogUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Shiro Realm 1: Token 登录验证
 */
public class TokenShiroRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;

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
		LogUtils.getInstance().info("在 Token 验证中，开始 给用户赋予权限");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		TokenToken tokenToken = (TokenToken) principalCollection.getPrimaryPrincipal();
		User user = userService.findUserByUsername((String) tokenToken.getPrincipal());

		// 可以在这里进行缓存

		Role role = user.getRole();
		authorizationInfo.addRole(role.getRole());
		for (Permission permission : role.getPermissionList()) {
			authorizationInfo.addStringPermission(permission.getPermission());
		}

		return authorizationInfo;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		LogUtils.getInstance().info("开始 token 认证");
		TokenToken tokenToken = (TokenToken) authenticationToken;
		LogUtils.getInstance().info("从输入得到的用户名：" + tokenToken.getPrincipal());

		return new SimpleAuthenticationInfo(
				tokenToken,
				tokenToken.getCredentials(),
				getName()
		);
	}
}
