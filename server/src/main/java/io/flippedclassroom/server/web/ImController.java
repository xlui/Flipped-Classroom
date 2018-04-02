package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.im.Message;
import io.flippedclassroom.server.service.MessageService;
import io.flippedclassroom.server.util.LogUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller@CrossOrigin
public class ImController {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private MessageService messageService;
	private Logger logger = LogUtils.getInstance();

	@MessageMapping(value = "/broadcast")
	@SendTo(value = "/b")
	public void broadcast(Message message, @CurrentUser User user) {
		if (user != null) {
			System.out.println("用户信息：" + user.getUsername());
		} else {
			System.out.println("用户信息获取失败！");
		}
		System.out.println("收到消息：" + message.getContent());
	}
}
