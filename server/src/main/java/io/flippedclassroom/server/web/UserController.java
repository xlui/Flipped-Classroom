package io.flippedclassroom.server.web;

import io.flippedclassroom.server.config.PasswordToken;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.utils.AssertUtil;
import io.flippedclassroom.server.utils.JWTUtil;
import io.flippedclassroom.server.utils.LogUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String postLogin(@RequestBody User user) {
		AssertUtil.assertUsernamePasswordNotNull(user);

		// 用户登录
		PasswordToken passwordToken = new PasswordToken(user.getUsername(), user.getPassword());
		SecurityUtils.getSubject().login(passwordToken);

		User loginUser = userService.findUserByUsername(user.getUsername());
		String token = null;
		if (loginUser.getUsername().equals(user.getUsername())) {
			token = JWTUtil.sign(loginUser.getUsername(), loginUser.getPassword());
			LogUtil.getLogger().info("Generate Token!\n" + token);
		}
		return token;
	}
}
