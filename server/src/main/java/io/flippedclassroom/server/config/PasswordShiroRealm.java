package io.flippedclassroom.server.config;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.utils.LogUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
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
		LogUtil.getLogger().info("开始 用户名密码 认证");
		String username = (String) authenticationToken.getPrincipal();
		LogUtil.getLogger().info("从输入得到的用户名：" + username);
		User user = userService.findUserByUsername(username);

		if (user == null) {
			return null;
		}

		return new SimpleAuthenticationInfo(
				user,
				user.getPassword(),
				ByteSource.Util.bytes(user.getSalt()),
				getName()
		);
	}
}