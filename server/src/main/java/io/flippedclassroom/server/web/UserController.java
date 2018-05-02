package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.config.Const;
import io.flippedclassroom.server.config.token.PasswordToken;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.response.JsonResponse;
import io.flippedclassroom.server.exception.AssertException;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.RoleService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.*;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "用户", description = "目前包括：用户注册、用户登录、用户登出、检查Token有效性、查看用户资料、更新用户资料")
@RestController
@CrossOrigin
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RedisService redisService;
	private Logger logger = LogUtils.getInstance();

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "用户注册", httpMethod = "POST")
	@ApiImplicitParam(name = "user", value = "{\n" +
			"\"username\" : \"新用户的用户名\",\n" +
			"\"password\" : \"新用户的密码\",\n" +
			"\"role\": {\"role\": \"teacher或者student\"}\n}")
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse register(@RequestBody @NotNull User user) throws Throwable {
		// 确保输入非空
		AssertUtils.assertNotNull(new Http400BadRequestException("用户基本信息不能为空！"), user.getUsername(), user.getPassword(), user.getRole());
		String username = user.getUsername(), password = user.getPassword();
		AssertUtils.assertNotNUllElseThrow(user.getRole().getRole(), () -> new Http400BadRequestException("用户角色不能为空！"));
		// 确保用户名不存在
		AssertUtils.assertNullElseThrow(userService.findUserByUsername(username), () -> new Http400BadRequestException("账号已存在！"));

		User newUser = new User(username, password);
		EncryptUtils.encrypt(newUser);
		Role role = roleService.findRoleByRoleName(user.getRole().getRole());
		AssertUtils.assertNotNUllElseThrow(role, () -> new Http400BadRequestException("非法角色类型！"));
		newUser.setRole(role);
		userService.save(newUser);
		return new JsonResponse(Const.SUCCESS, "成功注册！");
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
	public Map postLogin(@RequestBody @NotNull User user) {
		try {
			AssertUtils.assertNotNull(user.getUsername());
			AssertUtils.assertNotNull(user.getPassword());
		} catch (AssertException e) {
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
		logger.info("生成 Token！\n" + token);
		logger.info("保存 用户名-Token 对到 Redis");
		redisService.saveWithExpire(loginUser.getUsername(), token, DateUtils.expire(), TimeUnit.MILLISECONDS);

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
					"  \"nickname\" : \"昵称\",\n" +
					"  \"gender\" : \"性别\",\n" +
					"  \"signature\" : \"个性签名\"\n" +
					"}")
	)
	public Map getProfile(@ApiIgnore @CurrentUser User user) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("nickname", user.getNickname());
		map.put("gender", user.getGender());
		map.put("signature", user.getSignature());
		return map;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "修改资料", httpMethod = "POST", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "user", value = "{\n" +
					"\"nickname\":\"大龙猫\",\n" +
					"\"gender\":\"不明\",\n" +
					"\"signature\":\"我是一只大龙猫\"\n" +
					"}", required = true, dataType = "string", paramType = "body")
	)
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse postProfile(@RequestBody User user, @ApiIgnore @CurrentUser User currentUser) {
		currentUser.setNickname(user.getNickname());
		currentUser.setGender(user.getGender());
		currentUser.setSignature(user.getSignature());
		userService.save(currentUser);
		return new JsonResponse(Const.SUCCESS, "成功修改资料");
	}
}
