package io.flippedclassroom.server.web;

import io.flippedclassroom.server.config.Const;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Api(tags = "工具Api", description = "现在包括：课堂随机点名")
@RestController
public class UtilController {
	@Autowired
	private CourseService courseService;

	@RequestMapping(value = "/course/{courseId}/roll", method = RequestMethod.GET)
	@ApiOperation(value = "随机点名", httpMethod = "GET")
	@ApiResponses(
			@ApiResponse(code = 200, message = "用户类")
	)
	public User roll(@PathVariable Long courseId) throws Http400BadRequestException {
		Course course = courseService.findById(courseId);
		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
		List<User> users = course.getUserList().parallelStream().filter(user -> user.getRole().getRole().equals(Const.Student)).collect(Collectors.toList());
		if (users.size() <= 0) {
			throw new Http400BadRequestException("当前课程人数过少(0)，不能进行随机点名！！");
		}
		return users.get(new Random().nextInt(users.size()));
	}
}
