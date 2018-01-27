package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentRole;
import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.config.Constant;
import io.flippedclassroom.server.entity.Comment;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.CommentService;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.UserService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/course")
@Api(tags = "课程管理", description = "课程相关的所有内容都需要 Token 验证。目前包括：列出所有课程、查找课程、更新课程、删除课程、创建课程、加入课程、查看课程评论、添加课程评论")
public class CourseController {
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;

	@RequestMapping
	@ApiOperation(value = "课程列表", httpMethod = "GET")
	@ApiResponse(code = 200, message = "课程列表，JSON形式")
	public List<Course> getCourses(@ApiIgnore @CurrentUser User user) {
		return user.getCourseList();
	}

	@RequestMapping(value = "/{courseID}", method = RequestMethod.GET)
	@ApiOperation(value = "通过课程 ID 查找课程", notes = "通过课程 ID 查询", httpMethod = "GET")
	@ApiResponse(code = 200, message = "JSON化的课程信息，是课程列表其中一个")
	public Course getCourse(@PathVariable Long courseID) {
		return courseService.findCourseById(courseID);
	}

	@RequestMapping(value = "/update/{courseID}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@RequiresPermissions("course:update")
	@ApiOperation(value = "更新课程信息", notes = "通过课程 ID 查询", httpMethod = "POST")
	@ApiImplicitParam(name = "course", value = "课程信息：\n{\n\"name\":\"新的课程名\", \n\"major\":\"新的课程所属专业\"\n}")
	@ApiResponses({
			@ApiResponse(code = 200, message = "成功更新课程"),
			@ApiResponse(code = 401, message = "无权更新课程"),
			@ApiResponse(code = 403, message = "身份验证失败"),
	})
	public JsonResponse updateCourse(@PathVariable Long courseID, @RequestBody Course course) {
		Course newCourse = courseService.findCourseById(courseID);
		newCourse.setName(course.getName());
		newCourse.setMajor(course.getMajor());
		courseService.save(newCourse);
		return new JsonResponse(Constant.SUCCESS, "Successfully update course info!");
	}

	@RequestMapping(value = "/delete/{courseID}", method = RequestMethod.GET)
	@RequiresPermissions("course:delete")
	@ApiOperation(value = "删除课程", notes = "对于学生，是课程中少了一个学生；对于教师，是整个课程没了", httpMethod = "GET")
	@ApiResponse(code = 200, message = "课程删除成功！")
	public JsonResponse deleteCourse(@PathVariable Long courseID, @ApiIgnore @CurrentUser User user, @ApiIgnore @CurrentRole String role) {
		Course course = courseService.findCourseById(courseID);

		try {
			if (role.equals("student")) {
				if (course != null) {
					user.getCourseList().remove(course);
					userService.save(user);
					return new JsonResponse(Constant.SUCCESS, "Successfully delete course!");
				} else {
					return new JsonResponse(Constant.FAILED, "Course id is invalid!");
				}
			} else {
				user.getCourseList().remove(course);
				userService.save(user);
				courseService.delete(course);
				return new JsonResponse(Constant.SUCCESS, "Successfully delete course!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse(Constant.FAILED, "Unknown exception happened during delete the course, please try again!");
		}
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@RequiresPermissions("course:create")
	@ApiOperation(value = "创建课程", notes = "只有教师可以创建课程，对于学生，应当访问 /join API", httpMethod = "POST")
	@ApiImplicitParam(name = "course", value = "课程信息：\n{\n\"name\":\"课程名\", \n\"major\":\"课程所属专业\"\n}")
	@ApiResponse(code = 200, message = "课程创建成功")
	public JsonResponse createCourse(@RequestBody Course course, @ApiIgnore @CurrentUser User user) {
		Course newCourse = new Course(course.getName(), course.getMajor());
		user.getCourseList().add(newCourse);
		userService.save(user);
		return new JsonResponse(Constant.SUCCESS, "Successfully create a new course");
	}

	@RequestMapping(value = "/join/{courseID}", method = RequestMethod.GET)
	@RequiresPermissions("course:join")
	@ApiOperation(value = "加入课程", notes = "只有学生可以加入课程", httpMethod = "GET")
	@ApiResponse(code = 200, message = "成功加入课程")
	public JsonResponse joinCourse(@PathVariable Long courseID, @ApiIgnore @CurrentUser User user) {
		Course course = courseService.findCourseById(courseID);
		if (course != null) {
			user.getCourseList().add(course);
			userService.save(user);
			return new JsonResponse(Constant.SUCCESS, "Successfully add course!");
		} else {
			return new JsonResponse(Constant.FAILED, "Course id is invalid!");
		}
	}

	@RequestMapping(value = "/comment/{courseID}", method = RequestMethod.GET)
	@RequiresPermissions("course:comment:view")
	@ApiOperation(value = "查看课程评论", httpMethod = "GET")
	@ApiResponse(code = 200, message = "评论列表")
	public List<Comment> courseComments(@PathVariable Long courseID) {
		Course course = courseService.findCourseById(courseID);
		if (course != null) {
			return course.getCommentList();
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/comment/{courseID}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@RequiresPermissions("course:comment:add")
	@ApiOperation(value = "评论课程", httpMethod = "POST")
	@ApiImplicitParam(name = "comment", value = "留下一个评论：\n{\n\"content\":\"评论内容\"\n, \"reply\":\"回复评论，不设置默认 -1\"\n}")
	@ApiResponse(code = 200, message = "成功评论课程")
	public JsonResponse commentCourse(@PathVariable Long courseID, @RequestBody Comment comment, @ApiIgnore @CurrentUser User user) {
		Course course = courseService.findCourseById(courseID);
		if (course != null) {
			Comment newComment = new Comment(comment.getContent());
			newComment.setUser(user);
			newComment.setCourse(course);
			newComment.setReply(comment.getReply());
			commentService.save(newComment);
			return new JsonResponse(Constant.SUCCESS, "Successfully add a comment!");
		} else {
			return new JsonResponse(Constant.FAILED, "Course id is invalid!");
		}
	}
}
