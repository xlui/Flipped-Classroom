package io.flippedclassroom.server.web;

import io.flippedclassroom.server.config.PasswordToken;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.utils.AssertUtil;
import io.flippedclassroom.server.utils.JWTUtil;
import io.flippedclassroom.server.utils.LogUtil;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "用户管理")
@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "用户名密码登录", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "string", paramType = "body", example = "1", defaultValue = "1"),
			@ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "body", example = "dev", defaultValue = "dev")
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "为用户生成的 Token"),
			@ApiResponse(code = 401, message = "认证失败！")
	})
	public String postLogin(@ApiIgnore @RequestBody User user) {
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
