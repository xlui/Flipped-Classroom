package io.flippedclassroom.server;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.RoleService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

@SpringBootApplication
@RestController
public class ServerApplication extends SpringBootServletInitializer {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private CourseService courseService;

	/**
	 * 负责数据库的初始化
	 */
	@ApiIgnore
	@RequestMapping("/init")
	public String init() {
		User userStudent = userService.findUserByUsername("1");
		User userTeacher = userService.findUserByUsername("2");
		Role roleStudent = roleService.findRoleByRoleName("student");
		Role roleTeacher = roleService.findRoleByRoleName("teacher");
		Course courseMath = courseService.findCourseByCourseName("数学");
		Course courseDataStructure = courseService.findCourseByCourseName("数据结构");
		Course courseDatabase = courseService.findCourseByCourseName("数据库");

		if (userStudent != null)
			userService.delete(userStudent);
		if (userTeacher != null)
			userService.delete(userTeacher);
		if (roleStudent != null)
			roleService.delete(roleStudent);
		if (roleTeacher != null)
			roleService.delete(roleTeacher);
		if (courseMath != null)
			courseService.delete(courseMath);
		if (courseDataStructure != null)
			courseService.delete(courseDataStructure);
		if (courseDatabase != null)
			courseService.delete(courseDatabase);

		userStudent = new User("1", "dev");
		userTeacher = new User("2", "std");
		roleStudent = new Role("student");
		roleTeacher = new Role("teacher");
		courseMath = new Course("数学", "数学专业");
		courseDataStructure = new Course("数据结构", "计算机专业");
		courseDatabase = new Course("数据库", "计算机专业");

		EncryptUtil.encrypt(userStudent);
		EncryptUtil.encrypt(userTeacher);

		userStudent.setRole(roleStudent);
		userStudent.setCourseList(Arrays.asList(courseDataStructure, courseDatabase));
		userTeacher.setRole(roleTeacher);
		userTeacher.setCourseList(Arrays.asList(courseMath, courseDatabase, courseDataStructure));

		courseService.save(courseMath);
		courseService.save(courseDatabase);
		courseService.save(courseDataStructure);
		userService.save(userStudent);
		userService.save(userTeacher);
		roleService.save(roleStudent);
		roleService.save(roleTeacher);

		return "init success";
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ServerApplication.class);
	}
}
