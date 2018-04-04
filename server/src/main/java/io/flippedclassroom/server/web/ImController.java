package io.flippedclassroom.server.web;

import io.flippedclassroom.server.config.Const;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.im.Message;
import io.flippedclassroom.server.entity.im.MessageType;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import io.flippedclassroom.server.service.MessageService;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.AssertUtils;
import io.flippedclassroom.server.util.LogUtils;
import io.flippedclassroom.server.util.TokenUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.concurrent.TimeUnit;

@Controller
@CrossOrigin
public class ImController {
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	private Logger logger = LogUtils.getInstance();

	@MessageMapping(value = "/broadcast")
	@SendTo(value = "/b")
	public Message broadcast(Message msg, @Header(value = "Authorization") String token) throws Http400BadRequestException {
		User user = this.redisAuthorization(token);
		logger.info("收到来自用户 [" + user.getUsername() + "] 的广播消息：" + msg.getContent());
		Message message = new Message(msg.getContent(), MessageType.BROADCAST);
		message.setUser(user);
		message.setMessageType(MessageType.BROADCAST);
		messageService.save(message);
		return message;
	}

	@MessageMapping(value = "/group/{courseId}")
	public void dynamicGroup(@DestinationVariable(value = "courseId") Long groupId, @Header(value = "Authorization") String token, Message msg) throws Http400BadRequestException {
		User user = this.redisAuthorization(token);
		Message message = new Message(msg.getContent(), MessageType.GROUP);
		message.setUser(user);
		message.setGroupId(groupId);
		messageService.save(message);
		simpMessagingTemplate.convertAndSend("/g/" + groupId, message);
	}

	private User redisAuthorization(String token) throws Http400BadRequestException {
		String username = redisService.get(token);
		if (username != null) {
			return userService.findUserByUsername(username);
		} else {
			return this.tokenAuthorization(token);
		}
	}

	// Token 认证
	private User tokenAuthorization(String token) throws Http400BadRequestException {
		Logger logger = LogUtils.getInstance();
		logger.info("进入自定义 Token 校验！");
		String username = TokenUtils.getUsername(token);
		String redisCheck = redisService.get(username);
		User user = userService.findUserByUsername(username);

		AssertUtils.assertEqualsElseThrow(redisCheck, token, () -> new Http400BadRequestException("Redis校验失败！Token 可能已过期，请重新获取！"));
		AssertUtils.assertNotNUllElseThrow(user, () -> new Http400BadRequestException("Token 内容非法！"));
		// 开始进行校验
		boolean result = TokenUtils.verify(token, username, user.getPassword());
		// 检查校验结果
		AssertUtils.assertTrueElseThrow(result, () -> new Http400BadRequestException("Token 校验失败！"));

		redisService.saveWithExpire(token, username, Const.expire(), TimeUnit.MILLISECONDS);
		return user;
	}
}
