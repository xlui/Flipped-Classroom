package io.flippedclassroom.server.web;

import io.flippedclassroom.server.config.Constant;
import io.flippedclassroom.server.config.PasswordToken;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.utils.AssertUtil;
import io.flippedclassroom.server.utils.EncryptUtil;
import io.flippedclassroom.server.utils.JWTUtil;
import io.flippedclassroom.server.utils.LogUtil;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户管理", description = "目前包括：用户注册、用户登录")
@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional
	@ApiOperation(value = "用户注册", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "user", value = "用户实体类，测试的时候手动输入 json 字符串：{\"username\":\"新用户的用户名\", \"password\":\"新用户的密码\"}"),
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "success"),
	})
	public JsonResponse register(@RequestBody User user) {
		User user1 = new User(user.getUsername(), user.getPassword());
		if (userService.findUserByUsername(user1.getUsername()) != null) {
			return new JsonResponse(Constant.status.FAILED, "账号已存在");
		} else {
			AssertUtil.assertUsernamePasswordNotNull(user1);
			EncryptUtil.encrypt(user1);
			userService.save(user1);
			return new JsonResponse(Constant.status.SUCCESS, "成功注册");
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "用户名密码登录", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "user", value = "用户实体类，测试的时候手动输入 json 字符串：{\"username\":\"1\", \"password\":\"dev\"}", required = true, dataType = "string", paramType = "body")
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "为用户生成的 Token"),
			@ApiResponse(code = 401, message = "权限认证失败！"),
			@ApiResponse(code = 403, message = "身份认证失败！"),
	})
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
