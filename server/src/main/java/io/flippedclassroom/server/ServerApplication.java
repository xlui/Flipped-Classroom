package io.flippedclassroom.server;

import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
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

@SpringBootApplication
@RestController
public class ServerApplication extends SpringBootServletInitializer {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	/**
	 * 负责数据库的初始化
	 */
	@ApiIgnore
	@RequestMapping("/init")
	public String init() {
		User student = new User("1", "dev");
		User teacher = new User("2", "std");
		EncryptUtil.encrypt(student);
		EncryptUtil.encrypt(teacher);
		Role roleStudent = new Role("student");
		Role roleTeacher = new Role("teacher");

		student.setRole(roleStudent);
		teacher.setRole(roleTeacher);

		userService.save(student);
		userService.save(teacher);

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
