package io.flippedclassroom.server.config;

import io.flippedclassroom.server.entity.Permission;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.utils.LogUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Shiro Realm 2: 用户名密码 登录验证
 */
public class PasswordShiroRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;

	/**
	 * 通过重写 supports 方法来避免重复校验
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof PasswordToken;
	}

	/**
	 * 重写 isPermitted 方法来检查进行授权的是否是 PasswordToken 类型，不是则退出。避免重复授权
	 */
	@Override
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		return principals.getPrimaryPrincipal() instanceof PasswordToken && super.isPermitted(principals, permission);
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		LogUtil.getLogger().info("在 用户名密码 验证中，开始 给用户赋予权限");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		PasswordToken passwordToken = (PasswordToken) principalCollection.getPrimaryPrincipal();
		User user = userService.findUserByUsername((String) passwordToken.getPrincipal());

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
		LogUtil.getLogger().info("开始 用户名密码 认证");
		String username = (String) authenticationToken.getPrincipal();
		LogUtil.getLogger().info("从输入得到的用户名：" + username);
		User user = userService.findUserByUsername(username);

		if (user == null) {
			throw new AuthenticationException();
		}

		return new SimpleAuthenticationInfo(
				user,
				user.getPassword(),
				ByteSource.Util.bytes(user.getSalt()),
				getName()
		);
	}
}