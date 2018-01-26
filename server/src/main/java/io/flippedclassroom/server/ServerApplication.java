package io.flippedclassroom.server;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Permission;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.PermissionService;
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
	@Autowired
	private PermissionService permissionService;

	/**
	 * 负责数据库的初始化
	 */
	@ApiIgnore
	@RequestMapping("/init")
	public String init() {
		User userStudent = userService.findUserByUsername("1");
		User userTeacher = userService.findUserByUsername("2");
		// 角色默认有三种：学生、教师、管理员
		Role roleStudent = roleService.findRoleByRoleName("student");
		Role roleTeacher = roleService.findRoleByRoleName("teacher");
		Role roleAdmin = roleService.findRoleByRoleName("admin");
		Course courseMath = courseService.findCourseByCourseName("数学");
		Course courseDataStructure = courseService.findCourseByCourseName("数据结构");
		Course courseDatabase = courseService.findCourseByCourseName("数据库");
		Permission permissionUpdate = permissionService.findPermissionByPermissionName("course:update");

		// 如果数据库中存在这些实体，删除并重新初始化
		if (userStudent != null)
			userService.delete(userStudent);
		if (userTeacher != null)
			userService.delete(userTeacher);
		if (roleStudent != null)
			roleService.delete(roleStudent);
		if (roleTeacher != null)
			roleService.delete(roleTeacher);
		if (roleAdmin != null)
			roleService.delete(roleAdmin);
		if (courseMath != null)
			courseService.delete(courseMath);
		if (courseDataStructure != null)
			courseService.delete(courseDataStructure);
		if (courseDatabase != null)
			courseService.delete(courseDatabase);
		if (permissionUpdate != null)
			permissionService.delete(permissionUpdate);

		// 重新初始化
		userStudent = new User("1", "dev");
		userTeacher = new User("2", "std");
		roleStudent = new Role("student");
		roleTeacher = new Role("teacher");
		roleAdmin = new Role("admin");
		courseMath = new Course("数学", "数学专业");
		courseDataStructure = new Course("数据结构", "计算机专业");
		courseDatabase = new Course("数据库", "计算机专业");
		permissionUpdate = new Permission("course:update");

		// 密码加密存储
		EncryptUtil.encrypt(userStudent);
		EncryptUtil.encrypt(userTeacher);

		// 设置关系
		userStudent.setRole(roleStudent);
		userStudent.setCourseList(Arrays.asList(courseDataStructure, courseDatabase));
		userTeacher.setRole(roleTeacher);
		userTeacher.setCourseList(Arrays.asList(courseMath, courseDatabase, courseDataStructure));
		permissionUpdate.setRole(roleTeacher);

		// 保存
		courseService.save(courseMath);
		courseService.save(courseDatabase);
		courseService.save(courseDataStructure);
		userService.save(userStudent);
		userService.save(userTeacher);
		roleService.save(roleStudent);
		roleService.save(roleTeacher);
		roleService.save(roleAdmin);
		permissionService.save(permissionUpdate);

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
