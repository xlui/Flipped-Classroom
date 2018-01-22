package io.flippedclassroom.server.web;

import io.flippedclassroom.server.config.Constant;
import io.flippedclassroom.server.config.PasswordToken;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.RoleService;
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

import java.util.LinkedHashMap;
import java.util.Map;

@Api(tags = "用户管理", description = "目前包括：用户注册、用户登录")
@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RedisService redisService;

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional
	@ApiOperation(value = "用户注册", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "user", value = "用户实体类，测试的时候手动输入 json 字符串：" +
					"\n{\n\t\"username\": \"新用户的用户名\",\n\t\"password\": \"新用户的密码\",\n\t\"role\": {\n\t\t\"role\": \"teacher/student\"\n}\n}"),
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "成功注册"),
	})
	public JsonResponse register(@RequestBody User user) {
		User newUser = new User(user.getUsername(), user.getPassword());
		if (userService.findUserByUsername(newUser.getUsername()) != null) {
			return new JsonResponse(Constant.FAILED, "账号已存在");
		} else {
			AssertUtil.assertUsernamePasswordNotNull(newUser);
			EncryptUtil.encrypt(newUser);

			Role role = roleService.findRoleByRoleName(user.getRole().getRole());
			newUser.setRole(role);
			userService.save(newUser);
			return new JsonResponse(Constant.SUCCESS, "成功注册");
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
	public Map postLogin(@RequestBody User user) {
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
		redisService.save(loginUser.getUsername(), token);
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("status", Constant.SUCCESS);
		map.put("token", token);
		map.put("role", loginUser.getRole().getRole());
		return map;
	}
}
