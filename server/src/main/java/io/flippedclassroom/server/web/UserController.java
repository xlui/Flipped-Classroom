package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.config.Const;
import io.flippedclassroom.server.config.token.PasswordToken;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.exception.InputException;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.RoleService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.AssertUtils;
import io.flippedclassroom.server.util.EncryptUtils;
import io.flippedclassroom.server.util.LogUtils;
import io.flippedclassroom.server.util.TokenUtils;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Api(tags = "用户管理", description = "目前包括：用户注册、用户登录、用户登出、检查Token有效性、查看用户资料、更新用户资料")
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
	@ApiImplicitParam(name = "user", value = "{\n" +
			"\"username\" : \"新用户的用户名\",\n" +
			"\"password\" : \"新用户的密码\",\n" +
			"\"role\": {\"role\": \"teacher或者student\"}\n}")
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse register(@RequestBody User user) throws InputException {
		AssertUtils.assertUsernamePasswordNotNull(user);
		User newUser = new User(user.getUsername(), user.getPassword());
		if (userService.findUserByUsername(newUser.getUsername()) != null) {
			return new JsonResponse(Const.FAILED, "账号已存在");
		} else {
			EncryptUtils.encrypt(newUser);

			try {
				Role role = roleService.findRoleByRoleName(user.getRole().getRole());
				if (role == null) {
					return new JsonResponse(Const.FAILED, "非法角色类型");
				}
				newUser.setRole(role);
				userService.save(newUser);
				return new JsonResponse(Const.SUCCESS, "成功注册");
			} catch (NullPointerException e) {
				return new JsonResponse(Const.FAILED, "非法角色类型");
			}
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "用户名密码登录", httpMethod = "POST")
	@ApiImplicitParam(name = "user", value = "{\n" +
			"\"username\":\"1\",\n " +
			"\"password\":\"dev\" \n" +
			"}", required = true, dataType = "string", paramType = "body")
	@ApiResponses({
			@ApiResponse(code = 200, message = "自定义的 Map 的 JSON 化：\n{\n" +
					"  \"status\" : \"SUCCESS\",\n" +
					"  \"token\" : \"Token\",\n" +
					"  \"role\" : \"student\"\n" +
					"}"),
			@ApiResponse(code = 401, message = "权限认证失败！"),
			@ApiResponse(code = 403, message = "身份认证失败！"),
	})
	public Map postLogin(@RequestBody User user) {
		try {
			AssertUtils.assertUsernamePasswordNotNull(user);
		} catch (InputException e) {
			Map<String, String> map = new HashMap<>();
			map.put("status", Const.FAILED);
			map.put("message", "用户名或密码不合法！");
			return map;
		}

		// 用户登录
		PasswordToken passwordToken = new PasswordToken(user.getUsername(), user.getPassword());
		SecurityUtils.getSubject().login(passwordToken);

		User loginUser = userService.findUserByUsername(user.getUsername());
		String token = TokenUtils.sign(loginUser.getUsername(), loginUser.getPassword());
		LogUtils.getInstance().info("生成 Token！\n" + token);
		LogUtils.getInstance().info("保存 用户名-Token 对到 Redis");
		redisService.save(loginUser.getUsername(), token);

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("status", Const.SUCCESS);
		map.put("token", token);
		map.put("role", loginUser.getRole().getRole());
		return map;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ApiOperation(value = "登出，会删除保存的 Token", httpMethod = "GET", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse logout(@ApiIgnore @CurrentUser User user) {
		redisService.delete(user.getUsername());
		return new JsonResponse(Const.SUCCESS, "Logout success");
	}

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	@ApiOperation(value = "检查 Token 的有效性", httpMethod = "GET", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse check() {    // 也用作 Token 登录校验
		return new JsonResponse(Const.SUCCESS, "Token is valid");
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ApiOperation(value = "用户个人资料", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
	@ApiResponses(
			@ApiResponse(code = 200, message = "用户资料：\n{\n" +
					"  \"nick_name\" : \"昵称\",\n" +
					"  \"gender\" : \"性别\",\n" +
					"  \"signature\" : \"个性签名\"\n" +
					"}")
	)
	public Map getProfile(@ApiIgnore @CurrentUser User user) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("nick_name", user.getNick_name());
		map.put("gender", user.getGender());
		map.put("signature", user.getSignature());
		return map;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "修改资料", httpMethod = "POST", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
	@ApiImplicitParam(name = "user", value = "{\n\"nick_name\":\"大龙猫\",\n\"gender\":\"不明\",\n\"signature\":\"我是一只大龙猫\"\n}", required = true, dataType = "string", paramType = "body")
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse postProfile(@RequestBody User user, @ApiIgnore @CurrentUser User currentUser) {
		currentUser.setNick_name(user.getNick_name());
		currentUser.setGender(user.getGender());
		currentUser.setSignature(user.getSignature());
		userService.save(currentUser);
		return new JsonResponse(Const.SUCCESS, "成功修改资料");
	}
}
