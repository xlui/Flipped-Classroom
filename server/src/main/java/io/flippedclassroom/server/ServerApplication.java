package io.flippedclassroom.server;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.entity.Comment;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.*;
import io.flippedclassroom.server.util.EncryptUtils;
import io.flippedclassroom.server.util.LogUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
@ApiIgnore
public class ServerApplication extends SpringBootServletInitializer implements CommandLineRunner {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private CommentService commentService;

	@RequestMapping("/")
	public String index() {
		return "<html>\n" +
				"<head>\n" +
				"    <meta charset=\"utf-8\"/>\n" +
				"    <title>翻转课堂 —— 创造全新的课堂体验</title>\n" +
				"</head>\n" +
				"<body>\n" +
				"<div align=\"center\">\n" +
				"    翻转课堂 —— 创造全新的课堂体验\n" +
				"</div>\n" +
				"<hr/>\n" +
				"<br/>\n" +
				"<div>\n" +
				"    <ul>\n" +
				"        <li><p>访问网站：<a href=\"https://web.fc.xd.style/\" target=\"_blank\">https://web.fc.xd.style</a></p></li>\n" +
				"        <li><p>API 地址：<a href=\"https://api.fc.xd.style/\" target=\"_blank\">https://api.fc.xd.style</a></p></li>\n" +
				"        <li><p>API 说明：<a href=\"https://api.fc.xd.style/swagger-ui.html\" target=\"_blank\">https://api.fc.xd.style/swagger-ui.html</a>\n" +
				"        </p></li>\n" +
				"    </ul>\n" +
				"</div>\n" +
				"</body>\n" +
				"</html>\n";
	}

	/**
	 * 测试 Token 的有效性
	 */
	@RequestMapping("/hello")
	public String hello(@CurrentUser User user) {
		String username = user.getUsername();
		if (username != null) {
			return "Hello " + username;
		}
		return "Hello World!";
	}

	private void userInit() {
		User userStudent1 = userService.findUserByUsername("1");
		User userStudent2 = userService.findUserByUsername("3");
		User userTeacher = userService.findUserByUsername("2");

		if (userStudent1 == null) {
			userStudent1 = new User("1", "dev");
			EncryptUtils.encrypt(userStudent1);
			userService.save(userStudent1);
		}
		if (userStudent2 == null) {
			userStudent2 = new User("3", "dev");
			EncryptUtils.encrypt(userStudent2);
			userService.save(userStudent2);
		}
		if (userTeacher == null) {
			userTeacher = new User("2", "std");
			EncryptUtils.encrypt(userTeacher);
			userService.save(userTeacher);
		}
	}

	private void roleInit() {
		User userStudent1 = userService.findUserByUsername("1");
		User userStudent2 = userService.findUserByUsername("3");
		User userTeacher = userService.findUserByUsername("2");
		Role roleStudent = roleService.findRoleByRoleName("student");
		Role roleTeacher = roleService.findRoleByRoleName("teacher");
		Role roleAdmin = roleService.findRoleByRoleName("admin");

		if (roleAdmin == null) {
			roleAdmin = new Role("admin");
			roleService.save(roleAdmin);
		}
		if (roleStudent == null) {
			roleStudent = new Role("student");
			roleService.save(roleStudent);
			userStudent1.setRole(roleStudent);
			userService.save(userStudent1);
			userStudent2.setRole(roleStudent);
			userService.save(userStudent2);
		}
		if (roleTeacher == null) {
			roleTeacher = new Role("teacher");
			roleService.save(roleTeacher);
			userTeacher.setRole(roleTeacher);
			userService.save(userTeacher);
		}
	}

	private void courseInit() {
		User userStudent1 = userService.findUserByUsername("1");
		User userStudent2 = userService.findUserByUsername("3");
		User userTeacher = userService.findUserByUsername("2");
		Course courseMath = courseService.findCourseByCourseName("数学");
		Course courseDataStructure = courseService.findCourseByCourseName("数据结构");
		Course courseDatabase = courseService.findCourseByCourseName("数据库");

		if (courseMath == null) {
			courseMath = new Course("数学", "数学专业");
			courseService.save(courseMath);
		}
		if (courseDataStructure == null) {
			courseDataStructure = new Course("数据结构", "计算机专业");
			courseService.save(courseDataStructure);
		}
		if (courseDatabase == null) {
			courseDatabase = new Course("数据库", "计算机专业");
			courseService.save(courseDatabase);
		}
		userStudent1.setCourseList(new ArrayList<>(Arrays.asList(courseMath, courseDatabase, courseDataStructure)));
		userService.save(userStudent1);
		userStudent2.setCourseList(new ArrayList<>(Arrays.asList(courseDataStructure, courseDatabase)));
		userService.save(userStudent2);
		userTeacher.setCourseList(new ArrayList<>(Arrays.asList(courseMath, courseDatabase, courseDataStructure)));
		userService.save(userTeacher);
	}

	private void commentInit() {
		User userStudent1 = userService.findUserByUsername("1");
		User userStudent2 = userService.findUserByUsername("3");
		User userTeacher = userService.findUserByUsername("2");
		Course courseMath = courseService.findCourseByCourseName("数学");
		Course courseDataStructure = courseService.findCourseByCourseName("数据结构");
		Course courseDatabase = courseService.findCourseByCourseName("数据库");
		List<Comment> comments = commentService.findCommentsByCourse(courseMath);
		Comment commentFirst = null, commentSecond = null;

		if (comments.size() > 0) {
			commentFirst = comments.get(0);
			commentSecond = comments.get(1);
		}
		if (commentFirst != null) {
			commentService.delete(commentFirst);
		}
		if (commentSecond != null) {
			commentService.delete(commentSecond);
		}

		commentFirst = new Comment("Hello World!");
		commentService.save(commentFirst);
		commentSecond = new Comment("这是第二条评论");
		commentService.save(commentSecond);

		commentFirst.setUser(userStudent1);
		commentFirst.setCourse(courseMath);
		commentSecond.setUser(userTeacher);
		commentSecond.setCourse(courseMath);

		commentService.save(commentFirst);
		commentSecond.setReply(commentFirst.getId());
		commentService.save(commentSecond);
	}

	/**
	 * 负责数据库的初始化
	 */
	@RequestMapping("/init")
	@Transactional
	public String init() {
		Logger logger = LogUtils.getInstance();
		logger.info("初始化用户信息....");
		this.userInit();
		logger.info("初始化角色信息....");
		this.roleInit();
		logger.info("初始化课程信息....");
		this.courseInit();
		logger.info("初始化评论....");
		this.commentInit();
		return "init success";
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ServerApplication.class);
	}

	@Override
	public void run(String... strings) {
		System.out.println("测试命令行代码");
	}
}
