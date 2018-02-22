package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.config.Constant;
import io.flippedclassroom.server.config.PasswordToken;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.exception.InputException;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.RoleService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.AssertUtils;
import io.flippedclassroom.server.util.EncryptUtils;
import io.flippedclassroom.server.util.JWTUtils;
import io.flippedclassroom.server.util.LogUtils;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.LinkedHashMap;
import java.util.Map;

@Api(tags = "用户管理", description = "目前包括：用户注册、用户登录、检查Token有效性、查看用户资料、更新用户资料")
@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RedisService redisService;

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "用户注册", httpMethod = "POST")
	@ApiImplicitParam(name = "user", value = "\\{\n\"username\": \"新用户的用户名\",\n\"password\": \"新用户的密码\"," +
			"\n\"role\": {\"role\": \"teacher或者student\"}\n}")
	@ApiResponse(code = 200, message = "成功注册")
	public JsonResponse register(@RequestBody User user) throws InputException {
		AssertUtils.assertUsernamePasswordNotNull(user);
		User newUser = new User(user.getUsername(), user.getPassword());
		if (userService.findUserByUsername(newUser.getUsername()) != null) {
			return new JsonResponse(Constant.FAILED, "账号已存在");
		} else {
			EncryptUtils.encrypt(newUser);

			try {
				Role role = roleService.findRoleByRoleName(user.getRole().getRole());
				if (role == null) {
					return new JsonResponse(Constant.FAILED, "非法角色类型");
				}
				newUser.setRole(role);
				userService.save(newUser);
				return new JsonResponse(Constant.SUCCESS, "成功注册");
			} catch (NullPointerException e) {
				return new JsonResponse(Constant.FAILED, "非法角色类型");
			}
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "用户名密码登录", httpMethod = "POST")
	@ApiImplicitParam(name = "user", value = "{\n\"username\":\"1\",\n \"password\":\"dev\" \n}", required = true, dataType = "string", paramType = "body")
	@ApiResponses({
			@ApiResponse(code = 200, message = "为用户生成的 Token"),
			@ApiResponse(code = 401, message = "权限认证失败！"),
			@ApiResponse(code = 403, message = "身份认证失败！"),
	})
	public Map postLogin(@RequestBody User user) throws InputException {
		AssertUtils.assertUsernamePasswordNotNull(user);

		// 用户登录
		PasswordToken passwordToken = new PasswordToken(user.getUsername(), user.getPassword());
		SecurityUtils.getSubject().login(passwordToken);

		User loginUser = userService.findUserByUsername(user.getUsername());
		String token = JWTUtils.sign(loginUser.getUsername(), loginUser.getPassword());
		LogUtils.getInstance().info("生成 Token！\n" + token);
		LogUtils.getInstance().info("保存 用户名-Token 对到 Redis");
		redisService.save(loginUser.getUsername(), token);

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("status", Constant.SUCCESS);
		map.put("token", token);
		map.put("role", loginUser.getRole().getRole());
		return map;
	}

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	@ApiOperation(value = "检查 Token 的有效性", httpMethod = "GET")
	@ApiResponse(code = 200, message = "Token 有效")
	public JsonResponse check() {    // 也用作 Token 登录校验
		return new JsonResponse(Constant.SUCCESS, "Token is valid");
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ApiOperation(value = "用户个人资料", notes = "需要 Token 验证")
	@ApiResponse(code = 200, message = "json 化的用户信息")
	public Map getProfile(@ApiIgnore @CurrentUser User user) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("nick_name", user.getNick_name());
		map.put("gender", user.getGender());
		map.put("signature", user.getSignature());
		return map;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "修改资料", httpMethod = "POST", notes = "需要 Token 验证")
	@ApiImplicitParam(name = "user", value = "{\n\"nick_name\":\"大龙猫\",\n\"gender\":\"不明\",\n\"signature\":\"我是一只大龙猫\"\n}", required = true, dataType = "string", paramType = "body")
	@ApiResponses({
			@ApiResponse(code = 200, message = "成功修改资料！"),
			@ApiResponse(code = 401, message = "权限认证失败！"),
			@ApiResponse(code = 403, message = "身份认证失败！"),
	})
	public JsonResponse postProfile(@RequestBody User user, @ApiIgnore @CurrentUser User currentUser) {
		currentUser.setNick_name(user.getNick_name());
		currentUser.setGender(user.getGender());
		currentUser.setSignature(user.getSignature());
		userService.save(currentUser);
		return new JsonResponse(Constant.SUCCESS, "成功修改资料");
	}
}
