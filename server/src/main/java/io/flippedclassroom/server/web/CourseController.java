package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.CourseService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/course")
@Api(tags = "课程管理", description = "课程相关的所有内容都需要 Token 验证。目前包括：列出所有课程、通过 ID 查找课程、通过 ID 更新课程")
public class CourseController {
	@Autowired
	private CourseService courseService;

	@RequestMapping
	@ApiOperation(value = "课程列表", httpMethod = "GET")
	@ApiResponse(code = 200, message = "课程列表，JSON形式")
	public List<Course> getCourses(@ApiIgnore @CurrentUser User user) {
		return user.getCourseList();
	}

	@RequestMapping("/{courseID}")
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
	public String updateCourse(@PathVariable Long courseID, @RequestBody Course course, @ApiIgnore @CurrentUser User user) {
		return "尝试更新课程信息，发送的课程 ID 为：" + courseID + "\n新的课程信息为：\n" + "课程名：" + course.getName() + "\t课程所属专业" + course.getMajor();
	}
}
