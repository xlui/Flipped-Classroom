package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.config.Constant;
import io.flippedclassroom.server.entity.*;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.EDataService;
import io.flippedclassroom.server.service.PreviewService;
import io.flippedclassroom.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "文件管理", description = "目前包括：拉取头像、头像上传")
public class FileController {
	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private PreviewService previewService;
	@Autowired
	private EDataService eDataService;

	@RequestMapping(value = "/avatar", method = RequestMethod.GET)
	@ApiOperation(value = "获取头像")
	@ApiResponse(code = 200, message = "成功获取头像")
	public void getAvatar(@ApiIgnore @CurrentUser User user, HttpServletResponse response) {
//		todo: Test this API
		String avatarPosition = user.getAvatar();
		if (avatarPosition == null) {
			try {
				URL url = new URL(Constant.defaultAvatarLink.replace("MD5", MD5Encoder.encode(user.getId().toString().getBytes())));
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				response.setContentType(MediaType.IMAGE_JPEG_VALUE);
				IOUtils.copy(connection.getInputStream(), response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				InputStream in = new FileInputStream(new File(user.getAvatar()));
				response.setContentType(MediaType.IMAGE_JPEG_VALUE);
				IOUtils.copy(in, response.getOutputStream());
			} catch (Exception ignored) {

			}
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

	@RequestMapping(value = "/course/{courseID}/data/preview", method = RequestMethod.GET)
	@ApiOperation(value = "显示所有的预习资料")
	@ApiResponse(code = 200, message = "所有的预习资料")
	public Map getPreviews(@PathVariable Long courseID) {
		Map<String, Object> map = new HashMap<>();
		Course course = courseService.findCourseById(courseID);
		if (course == null) {
			map.put("status", Constant.FAILED);
			map.put("message", "course id is invalid!");
			return map;
		}
		map.put("status", Constant.SUCCESS);
		map.put("preview", course.getPreviewList());
		return map;
	}

	@RequestMapping(value = "/course/{courseID}/data/preview/{previewID}", method = RequestMethod.GET)
	@ApiOperation(value = "下载预习资料")
	@ApiResponse(code = 200, message = "成功下载预习资料")
	public JsonResponse getPreview(@PathVariable Long courseID, @PathVariable Long previewID) {
//		todo: 下载预习资料
		return new JsonResponse(Constant.SUCCESS, "Helo");
	}


	@RequestMapping(value = "/course/{courseID}/data/preview", method = RequestMethod.POST)
	@RequiresPermissions("course:data:upload")
	@ApiOperation(value = "课前预习资料")
	@ApiResponse(code = 200, message = "成功上传课前预习资料")
	public JsonResponse postPreview(@RequestPart(name = "file", required = true) MultipartFile multipartFile, @ApiIgnore @CurrentUser User user, @PathVariable Long courseID) {
		if (multipartFile == null) {
			return new JsonResponse(Constant.FAILED, "upload file is null OR course id is invalid!");
		}
		Course course = courseService.findCourseById(courseID);
		if (course == null) {
			return new JsonResponse(Constant.FAILED, "upload file is null OR course id is invalid!");
		}
		String coursePreviewPosition = Constant.coursePreviewPosition + "course-" + courseID + "/" + multipartFile.getOriginalFilename();
		Preview preview = new Preview(coursePreviewPosition);
		try {
			FileUtils.writeByteArrayToFile(new File(coursePreviewPosition), multipartFile.getBytes());
			preview.setCourse(course);
			previewService.save(preview);
			return new JsonResponse(Constant.SUCCESS, "Successfully save preview data!");
		} catch (IOException e) {
			e.printStackTrace();
			return new JsonResponse(Constant.FAILED, "IOException occurs during saving preview file!");
		}
	}

	@RequestMapping(value = "/course/{courseID}/data/edata", method = RequestMethod.POST)
	@RequiresPermissions("course:data:upload")
	@ApiOperation(value = "电子资料")
	@ApiResponse(code = 200, message = "成功上传电子资料")
	public JsonResponse postEData(@RequestPart(name = "file") MultipartFile multipartFile, @PathVariable Long courseID) {
		if (multipartFile == null) {
			return new JsonResponse(Constant.FAILED, "upload file is null OR course id is invalid!");
		}
		Course course = courseService.findCourseById(courseID);
		if (course == null) {
			return new JsonResponse(Constant.FAILED, "upload file is null OR course id is invalid!");
		}
		String courseEDataPosition = Constant.courseEDataPosition + "course-" + courseID + "/" + multipartFile.getOriginalFilename();
		EData eData = new EData(courseEDataPosition);
		try {
			// 写入文件
			FileUtils.writeByteArrayToFile(new File(courseEDataPosition), multipartFile.getBytes());
			// 更新数据库
			eData.setCourse(course);
			eDataService.save(eData);
			return new JsonResponse(Constant.SUCCESS, "Successfully save E-Data!");
		} catch (IOException e) {
			e.printStackTrace();
			return new JsonResponse(Constant.FAILED, "IOException occurs during saving E-Data!");
		}
	}
}
