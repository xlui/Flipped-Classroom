package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.config.Constant;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@Api(tags = "文件管理", description = "目前包括：拉取头像、头像上传")
public class FileController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/avatar", method = RequestMethod.GET)
	@ApiOperation(value = "获取头像")
	@ApiResponse(code = 200, message = "成功获取头像")
	public void getAvatar(@ApiIgnore @CurrentUser User user, HttpServletResponse response) {
//		todo: 测试是否成功显示
		response.setContentType("image/png");
		try {
			response.getOutputStream().write(new FileInputStream(new File(user.getAvatar())).read());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/avatar", method = RequestMethod.POST)
	@ApiOperation(value = "设置头像")
	@ApiResponse(code = 200, message = "成功设置头像")
	public JsonResponse postAvatar(@RequestPart(name = "file", required = false) MultipartFile multipartFile, @ApiIgnore @CurrentUser User user) {
		if (multipartFile == null) {
			return new JsonResponse(Constant.FAILED, "upload file is null!");
		} else {
			try {
				String avatarPosition = Constant.avatarPosition + "user" + user.getId();
				FileUtils.writeByteArrayToFile(new File(avatarPosition), multipartFile.getBytes());
				user.setAvatar(avatarPosition);
				userService.save(user);
				return new JsonResponse(Constant.SUCCESS, "Successfully set avatar!");
			} catch (IOException e) {
				e.printStackTrace();
				return new JsonResponse(Constant.FAILED, "Something error during saving avatar! Please try later!");
			}
		}
	}
}
