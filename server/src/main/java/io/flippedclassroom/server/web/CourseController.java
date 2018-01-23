package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/course")
@Api(tags = "课程管理", description = "目前包括：列出用户所有课程")
public class CourseController {
	@Autowired
	private CourseService courseService;

	@RequestMapping
	@ApiOperation(value = "课程列表", httpMethod = "GET")
	@ApiResponse(code = 200, message = "课程列表，JSON形式")
	public List<Course> getCourses(@ApiIgnore @CurrentUser User user) {
		return user.getCourseList();
	}


}
