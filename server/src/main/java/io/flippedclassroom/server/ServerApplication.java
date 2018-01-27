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
import io.flippedclassroom.server.utils.LogUtil;
import org.slf4j.Logger;
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
		Logger logger = LogUtil.getLogger();

		User userStudent = userService.findUserByUsername("1");
		User userTeacher = userService.findUserByUsername("2");
		// 角色默认有三种：学生、教师、管理员
		Role roleStudent = roleService.findRoleByRoleName("student");
		Role roleTeacher = roleService.findRoleByRoleName("teacher");
		Role roleAdmin = roleService.findRoleByRoleName("admin");
		// 课程初始化
		Course courseMath = courseService.findCourseByCourseName("数学");
		Course courseDataStructure = courseService.findCourseByCourseName("数据结构");
		Course courseDatabase = courseService.findCourseByCourseName("数据库");
		// 权限初始化
		Permission permissionUpdate = permissionService.findPermissionByPermissionName("course:update");
		Permission permissionDelete = permissionService.findPermissionByPermissionName("course:delete");
		Permission permissionCreate = permissionService.findPermissionByPermissionName("course:create");
		Permission permissionJoin = permissionService.findPermissionByPermissionName("course:join");
		Permission permissionViewComment = permissionService.findPermissionByPermissionName("course:comment:view");
		Permission permissionAddComment = permissionService.findPermissionByPermissionName("course:comment:add");

		// 如果数据库中存在这些实体，删除并重新初始化
		logger.info("如果数据库中存在初始化信息，删除");
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
		if (permissionDelete != null)
			permissionService.delete(permissionDelete);
		if (permissionCreate != null)
			permissionService.delete(permissionCreate);
		if (permissionJoin != null)
			permissionService.delete(permissionJoin);
		if (permissionViewComment != null)
			permissionService.delete(permissionViewComment);
		if (permissionAddComment != null)
			permissionService.delete(permissionAddComment);

		// 重新初始化
		logger.info("重新初始化...");
		userStudent = new User("1", "dev");
		userTeacher = new User("2", "std");
		roleStudent = new Role("student");
		roleTeacher = new Role("teacher");
		roleAdmin = new Role("admin");
		courseMath = new Course("数学", "数学专业");
		courseDataStructure = new Course("数据结构", "计算机专业");
		courseDatabase = new Course("数据库", "计算机专业");
		permissionUpdate = new Permission("course:update");
		permissionDelete = new Permission("course:delete");
		permissionCreate = new Permission("course:create");
		permissionJoin = new Permission("course:join");
		permissionViewComment = new Permission("course:comment:view");
		permissionAddComment = new Permission("course:comment:add");

		// 密码加密存储
		EncryptUtil.encrypt(userStudent);
		EncryptUtil.encrypt(userTeacher);

		// 设置关系
		logger.info("设置关系");
		userStudent.setRole(roleStudent);
		userStudent.setCourseList(Arrays.asList(courseDataStructure, courseDatabase));

		userTeacher.setRole(roleTeacher);
		userTeacher.setCourseList(Arrays.asList(courseMath, courseDatabase, courseDataStructure));

		permissionUpdate.setRoleList(Arrays.asList(roleTeacher, roleAdmin));
		permissionDelete.setRoleList(Arrays.asList(roleStudent, roleTeacher, roleAdmin));
		permissionCreate.setRoleList(Arrays.asList(roleTeacher, roleAdmin));
		permissionJoin.setRoleList(Arrays.asList(roleStudent, roleAdmin));
		permissionViewComment.setRoleList(Arrays.asList(roleStudent, roleTeacher, roleAdmin));
		permissionAddComment.setRoleList(Arrays.asList(roleStudent, roleTeacher, roleAdmin));

		// 保存
		logger.info("保存到数据库");
		courseService.save(courseMath);
		courseService.save(courseDatabase);
		courseService.save(courseDataStructure);
		userService.save(userStudent);
		userService.save(userTeacher);
		roleService.save(roleStudent);
		roleService.save(roleTeacher);
		roleService.save(roleAdmin);
		permissionService.save(permissionUpdate);
		permissionService.save(permissionDelete);
		permissionService.save(permissionCreate);
		permissionService.save(permissionJoin);
		permissionService.save(permissionViewComment);
		permissionService.save(permissionAddComment);

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
