package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentRole;
import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.config.Const;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.response.JsonResponse;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.AssertUtils;
import io.flippedclassroom.server.util.LogUtils;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/course")
@Api(tags = "课程管理", description = "课程相关的所有内容都需要 Token 验证。目前包括：列出所有课程、查找课程、更新课程、删除课程、创建课程、加入课程、查看课程评论、添加课程评论、更新课程评论、删除课程评论")
public class CourseController {
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	private Logger logger = LogUtils.getInstance();


	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "课程列表", httpMethod = "GET", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
	@ApiResponses(
			@ApiResponse(code = 200, message = "自定义的返回格式：\n{\n" +
					"  \"courses\" : [ {\n" +
					"    \"id\" : 1,\n" +
					"    \"name\" : \"数学\",\n" +
					"    \"major\" : \"数学专业\",\n" +
					"    \"picture\" : \"https://fc.xd.style/course/1/picture\",\n" +
					"    \"count\" : 2,\n" +
					"    \"code\" : \"xust1\"\n" +
					"  } ],\n" +
					"  \"status\" : \"SUCCESS\"\n" +
					"}")
	)
	public Map getCourses(@ApiIgnore @CurrentUser User user) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", Const.SUCCESS);
		map.put("courses", user.getCourseList());
		return map;
	}

	@RequestMapping(value = "/{courseID}", method = RequestMethod.GET)
	@ApiOperation(value = "通过课程 ID 查找课程", notes = "需要以 课程ID 构建 URL，例如 https://fc.xd.style/course/1", httpMethod = "GET")
	@ApiResponses(
			@ApiResponse(code = 200, message = "课程实体类：\n{\n" +
					"  \"id\" : 1,\n" +
					"  \"name\" : \"数学\",\n" +
					"  \"major\" : \"数学专业\",\n" +
					"  \"picture\" : \"https://fc.xd.style/course/1/picture\",\n" +
					"  \"count\" : 2,\n" +
					"  \"code\" : \"xust1\"\n" +
					"}")
	)
	public Course getCourse(@PathVariable Long courseID) throws Http400BadRequestException {
		Optional<Course> course = Optional.of(courseService.findById(courseID));
		return course.orElseThrow(() -> new Http400BadRequestException("课程 id 非法！"));
	}

	@RequestMapping(value = "/{courseID}/update", method = RequestMethod.POST)
	@ApiOperation(value = "更新课程信息", notes = "需要以 课程ID 构建 URL，例如 https://fc.xd.style/course/1/update", httpMethod = "POST")
	@ApiImplicitParam(name = "course", required = true, value = "课程信息JSON：\n{\n\"name\":\"新的课程名\", \n\"major\":\"新的课程所属专业\"\n}")
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse updateCourse(@PathVariable Long courseID, @RequestBody Course course) throws Http400BadRequestException {
		Course newCourse = courseService.findById(courseID);
		AssertUtils.assertNotNUllElseThrow(newCourse, () -> new Http400BadRequestException("课程 id 非法！"));
		newCourse.setName(course.getName());
		newCourse.setMajor(course.getMajor());
		courseService.save(newCourse);
		return new JsonResponse(Const.SUCCESS, "成功更新课程信息！");
	}

	@RequestMapping(value = "/{courseID}/delete", method = RequestMethod.GET)
	@ApiOperation(value = "删除课程", notes = "对于学生，是课程中少了一个学生；对于教师，是整个课程没了", httpMethod = "GET")
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse deleteCourse(@PathVariable Long courseID, @ApiIgnore @CurrentUser User user, @ApiIgnore @CurrentRole @NotNull String role) throws Http400BadRequestException {
		Course course = courseService.findById(courseID);

		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));

		if (role.equals(Const.Student)) {        // 学生删除课程逻辑
			logger.info("用户 " + user.getUsername() + " [角色：" + role + "] 删除了课程 " + course.getName());
			user.getCourseList().remove(course);
			userService.save(user);
			return new JsonResponse(Const.SUCCESS, "成功删除课程！");
		} else {
			logger.info("用户 " + user.getUsername() + "[角色：" + role + "] 删除了课程 " + course.getName());
			courseService.delete(course);
			return new JsonResponse(Const.SUCCESS, "成功删除课程！");
		}
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "创建课程", notes = "只有教师可以创建课程，对于学生，应当访问 /join API", httpMethod = "POST")
	@ApiImplicitParam(name = "course", value = "课程信息：\n{\n\"name\":\"课程名\", \n\"major\":\"课程所属专业\"\n}")
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse createCourse(@RequestBody Course course, @ApiIgnore @CurrentUser User user) {
		Course newCourse = new Course(course.getName(), course.getMajor());
		user.getCourseList().add(newCourse);
		userService.save(user);
		return new JsonResponse(Const.SUCCESS, "成功创建课程！");
	}

	@RequestMapping(value = "/{courseID}/join", method = RequestMethod.GET)
	@ApiOperation(value = "加入课程", notes = "只有学生可以加入课程", httpMethod = "GET")
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse joinCourse(@PathVariable Long courseID, @ApiIgnore @CurrentUser User user) throws Http400BadRequestException {
		Course course = courseService.findById(courseID);
		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
		List<Course> courses = user.getCourseList();
		AssertUtils.assertFalseElseThrow(courses.contains(course), () -> new Http400BadRequestException("不应该加入一个已有课程！"));
		courses.add(course);
		userService.save(user);
		return new JsonResponse(Const.SUCCESS, "成功加入课程！");
	}
}
