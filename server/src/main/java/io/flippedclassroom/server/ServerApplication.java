package io.flippedclassroom.server;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ServerApplication {
	@Autowired
	private UserService userService;

	/**
	 * 负责数据库的初始化
	 */
	@RequestMapping("/init")
	public String init() {
		User defaultUser = new User("1", "dev");
		EncryptUtil.encrypt(defaultUser);
		userService.save(defaultUser);

		return "init success";
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
