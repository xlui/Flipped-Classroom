package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.config.Const;
import io.flippedclassroom.server.entity.*;
import io.flippedclassroom.server.exception.PositionInvalidException;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.EDataService;
import io.flippedclassroom.server.service.PreviewService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.AssertUtils;
import io.flippedclassroom.server.util.ImageUtils;
import io.flippedclassroom.server.util.LogUtils;
import io.swagger.annotations.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin
@Api(tags = "文件管理", description = "目前包括：拉取头像、头像上传、拉取所有预习资料、下载特定 id 的预习资料、上传课程预习资料")
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
	@ApiOperation(value = "获取头像", httpMethod = "GET", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
	@ApiResponses(
			@ApiResponse(code = 200, message = "返回图片流，如果用户没有设置头像，默认从 Gravatar 加载头像")
	)
	public JsonResponse getAvatar(@ApiIgnore @CurrentUser User user, HttpServletResponse response) {
		String avatarPosition = user.getAvatar();
		try {
			try {
				// 保证位置合法
				AssertUtils.assertPositionValid(avatarPosition);
				InputStream in = new FileInputStream(user.getAvatar());
				response.setContentType(MediaType.IMAGE_PNG_VALUE);
				IOUtils.copy(in, response.getOutputStream());
				return new JsonResponse(Const.SUCCESS, "成功获得用户 " + user.getId() + "的头像！");
			} catch (PositionInvalidException e) {
				String md5 = DigestUtils.md5DigestAsHex(user.getId().toString().getBytes());
				URL url = new URL(Const.defaultAvatarLink.replace("MD5", md5));
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				response.setContentType(MediaType.IMAGE_PNG_VALUE);
				IOUtils.copy(connection.getInputStream(), response.getOutputStream());
				return new JsonResponse(Const.SUCCESS, "成功从 gravatar 生成头像！");
			}
		} catch (IOException ignored) {
			return new JsonResponse(Const.FAILED, "IOException!");
		}
	}

	@RequestMapping(value = "/avatar", method = RequestMethod.POST)
	@ApiOperation(value = "设置头像", httpMethod = "POST")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "file", value = "头像文件，默认会将非 PNG 格式的图像转换为 PNG 格式存储")
	)
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse postAvatar(@RequestPart(name = "file", required = false) @ApiIgnore MultipartFile multipartFile, @ApiIgnore @CurrentUser User user) {
		if (multipartFile == null) {
			return new JsonResponse(Const.FAILED, "上传的文件为空！");
		} else {
			try {
				String fileName = multipartFile.getOriginalFilename();
				String suffix = fileName.substring(fileName.lastIndexOf("."));
				String avatarPosition = Const.avatarPosition + "user-" + user.getId();
				LogUtils.getInstance().info("上传文件后缀名：" + suffix);

				FileUtils.writeByteArrayToFile(new File(avatarPosition + suffix), multipartFile.getBytes());

				if (!suffix.equals(".png")) {
					LogUtils.getInstance().info("将源文件后缀改为 png，并删除源文件");
					ImageUtils.convertFormat(avatarPosition + suffix, avatarPosition + ".png", "PNG");
					FileUtils.deleteQuietly(new File(avatarPosition + suffix));
				}

				user.setAvatar(avatarPosition + ".png");
				userService.save(user);
				return new JsonResponse(Const.SUCCESS, "成功设置头像！");
			} catch (IOException e) {
				e.printStackTrace();
				return new JsonResponse(Const.FAILED, "IOException!");
			}
		}
	}

	@RequestMapping(value = "/course/{courseID}/data/preview", method = RequestMethod.GET)
	@ApiOperation(value = "显示某一课程下所有的预习资料")
	@ApiResponses(
			@ApiResponse(code = 200, message = "当前课程下的所有的预习资料，position 字段是文件位置，为显示做了特别处理。示例：\n{\n" +
					"  \"preview\" : [ {\n" +
					"    \"id\" : 1,\n" +
					"    \"position\" : \"Java 8实战.pdf\"\n" +
					"  }, {\n" +
					"    \"id\" : 2,\n" +
					"    \"position\" : \"JAVA EE开发的颠覆者  SPRING BOOT实战.pdf\"\n" +
					"  } ],\n" +
					"  \"status\" : \"SUCCESS\"\n" +
					"}")
	)
	public Map getPreviews(@PathVariable Long courseID) {
		Map<String, Object> map = new HashMap<>();
		Course course = courseService.findCourseById(courseID);
		if (course == null) {
			map.put("status", Const.FAILED);
			map.put("message", "course id is invalid!");
			return map;
		}
		List<Preview> previews = course.getPreviewList();
		previews.parallelStream().forEach(preview -> preview.setPosition(preview.getPosition().substring(preview.getPosition().lastIndexOf("/") + 1)));
		map.put("status", Const.SUCCESS);
		map.put("preview", previews);
		return map;
	}

	@RequestMapping(value = "/course/{courseID}/data/preview/{previewID}", method = RequestMethod.GET)
	@ApiOperation(value = "下载预习资料")
	@ApiResponses(
			@ApiResponse(code = 200, message = "返回文件流，和图片加载的方式类似")
	)
	public JsonResponse getPreview(@PathVariable Long courseID, @PathVariable Long previewID, HttpServletResponse response) {
		Optional<Course> course = Optional.of(courseService.findCourseById(courseID));
		List<Preview> list;
		try {
			list = course.orElseThrow(Exception::new).getPreviewList();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse(Const.FAILED, "No such course!");
		}
		Optional<Preview> preview = list.parallelStream().filter(p -> p.getId().equals(previewID)).findFirst();
		if (preview.isPresent()) {
			try {
				String position = preview.get().getPosition();
				AssertUtils.assertPositionValid(position);
				InputStream in = new FileInputStream(position);
				response.setContentType("application/x-msdownload");
				IOUtils.copy(in, response.getOutputStream());
				return new JsonResponse(Const.SUCCESS, "成功下载 " + position.substring(position.lastIndexOf("/") + 1));
			} catch (PositionInvalidException e) {
				return new JsonResponse(Const.FAILED, "No such file!");
			} catch (IOException e) {
				LogUtils.getInstance().info("IOException during download the file: " + e.getMessage());
				return new JsonResponse(Const.FAILED, "IOException!!!");
			}
		} else {
			return new JsonResponse(Const.FAILED, "Preview id is invalid!");
		}
	}


	@RequestMapping(value = "/course/{courseID}/data/preview", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	@ApiOperation(value = "上传课前预习资料")
	@ApiResponses(
			@ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
	)
	public JsonResponse postPreview(@RequestParam(name = "file") @ApiIgnore MultipartFile multipartFile, @ApiIgnore @CurrentUser User user, @PathVariable Long courseID) {
		if (multipartFile == null) {
			return new JsonResponse(Const.FAILED, "upload file is null OR course id is invalid!");
		}
		Course course = courseService.findCourseById(courseID);
		if (course == null) {
			return new JsonResponse(Const.FAILED, "upload file is null OR course id is invalid!");
		}
		String coursePreviewPosition = Const.coursePreviewPosition + "course-" + courseID + "/" + multipartFile.getOriginalFilename();
		Preview preview = new Preview(coursePreviewPosition);
		try {
			FileUtils.writeByteArrayToFile(new File(coursePreviewPosition), multipartFile.getBytes());
			preview.setCourse(course);
			previewService.save(preview);
			return new JsonResponse(Const.SUCCESS, "Successfully save preview data!");
		} catch (IOException e) {
			LogUtils.getInstance().info("上传文件时发生 IOException: " + e.getMessage());
			return new JsonResponse(Const.FAILED, "IOException occurs during saving preview file!");
		}
	}

	@RequestMapping(value = "/course/{courseID}/data/edata", method = RequestMethod.POST)
	@ApiOperation(value = "电子资料")
	@ApiResponse(code = 200, message = "成功上传电子资料")
	public JsonResponse postEData(@RequestPart(name = "file") MultipartFile multipartFile, @PathVariable Long courseID) {
		if (multipartFile == null) {
			return new JsonResponse(Const.FAILED, "upload file is null OR course id is invalid!");
		}
		Course course = courseService.findCourseById(courseID);
		if (course == null) {
			return new JsonResponse(Const.FAILED, "upload file is null OR course id is invalid!");
		}
		String courseEDataPosition = Const.courseEDataPosition + "course-" + courseID + "/" + multipartFile.getOriginalFilename();
		EData eData = new EData(courseEDataPosition);
		try {
			// 写入文件
			FileUtils.writeByteArrayToFile(new File(courseEDataPosition), multipartFile.getBytes());
			// 更新数据库
			eData.setCourse(course);
			eDataService.save(eData);
			return new JsonResponse(Const.SUCCESS, "Successfully save E-Data!");
		} catch (IOException e) {
			e.printStackTrace();
			return new JsonResponse(Const.FAILED, "IOException occurs during saving E-Data!");
		}
	}

	@RequestMapping(value = "/course/{courseID}/picture", method = RequestMethod.GET)
	@ApiOperation(value = "获取课程图片")
	@ApiResponse(code = 200, message = "课程图片流")
	public JsonResponse getCoursePicture(@PathVariable Long courseID, HttpServletResponse response) {
		Course course = courseService.findCourseById(courseID);
		if (course != null) {
			String pictureLocation = course.getPicture();
			try {
				try {
					AssertUtils.assertPositionValid(pictureLocation);
					InputStream in = new FileInputStream(pictureLocation);
					response.setContentType(MediaType.IMAGE_PNG_VALUE);
					IOUtils.copy(in, response.getOutputStream());
					return new JsonResponse(Const.SUCCESS, "成功获取课程 " + course.getId() + " 的图片！");
				} catch (PositionInvalidException e) {
					String md5 = DigestUtils.md5DigestAsHex(course.getId().toString().getBytes());
					URL url = new URL(Const.defaultAvatarLink.replace("MD5", md5));
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					response.setContentType(MediaType.IMAGE_PNG_VALUE);
					IOUtils.copy(connection.getInputStream(), response.getOutputStream());
					return new JsonResponse(Const.SUCCESS, "成功为课程 " + course.getId() + "从 gravatar 生成图片！");
				}
			} catch (IOException e) {
				return new JsonResponse(Const.FAILED, "IOException!");
			}
		} else {
			return new JsonResponse(Const.FAILED, "Course Id 非法！");
		}
	}

	@RequestMapping(value = "/course/{courseID}/picture", method = RequestMethod.POST)
	@ApiOperation(value = "上传课程图片")
	@ApiResponse(code = 200, message = "Json Response")
	public JsonResponse postCoursePicture(@PathVariable Long courseID, @RequestPart(name = "file") MultipartFile multipartFile) {
		if (multipartFile == null) {
			return new JsonResponse(Const.FAILED, "上传文件为空或者 course id 非法！");
		}
		Course course = courseService.findCourseById(courseID);
		if (course == null) {
			return new JsonResponse(Const.FAILED, "上传文件为空或者 course id 非法！");
		}
		String coursePicturePosition = Const.coursePicture + "course-" + courseID;
		String fileName = multipartFile.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		LogUtils.getInstance().info("课程图像后缀名：" + suffix);
		try {
			FileUtils.writeByteArrayToFile(new File(coursePicturePosition + suffix), multipartFile.getBytes());
			if (suffix == null || !suffix.equals(".png")) {
				LogUtils.getInstance().info("将源文件后缀改为 png，并删除源文件");
				ImageUtils.convertFormat(coursePicturePosition + suffix, coursePicturePosition + ".png", "PNG");
				FileUtils.deleteQuietly(new File(coursePicturePosition + suffix));
			}
		} catch (IOException e) {
			return new JsonResponse(Const.FAILED, "IOException occur!");
		}
		course.setPicture(coursePicturePosition + ".png");
		courseService.save(course);
		return new JsonResponse(Const.SUCCESS, "成功设置课程图片！");
	}
}