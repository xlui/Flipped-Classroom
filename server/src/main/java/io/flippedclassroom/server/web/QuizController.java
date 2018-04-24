package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.config.Const;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Quiz;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.UserQuizResult;
import io.flippedclassroom.server.entity.response.JsonResponse;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.QuizService;
import io.flippedclassroom.server.service.UserQuizResultService;
import io.flippedclassroom.server.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "随堂测试", description = "目前包括：开启测试入口、关闭测试入口、获取测试题、提交单个测试题")
@RestController
public class QuizController {
	@Autowired
	private CourseService courseService;
	@Autowired
	private QuizService quizService;
	@Autowired
	private UserQuizResultService userQuizResultService;

	@RequestMapping(value = "/course/{courseId}/quiz/start", method = RequestMethod.GET)
	@ApiOperation(value = "教师请求此API打开学生端随堂测试入口", httpMethod = "GET")
	@ApiResponses(
			@ApiResponse(code = 200, message = "成功情况：{\n" +
					"\"status\": \"SUCCESS\",\n" +
					"\"message\": \"成功开启随堂测试入口！\"\n" +
					"}\n失败情况：{\n" +
					"\"status\": \"FAILED\",\n" +
					"\"message\": \"课程 id 非法！\"\n" +
					"}")
	)
	public JsonResponse startQuiz(@PathVariable Long courseId) throws Http400BadRequestException {
		Course course = courseService.findById(courseId);
		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
		course.setQuizEnable(true);
		courseService.save(course);
		return new JsonResponse(Const.SUCCESS, "成功开启随堂测试入口！");
	}

	@RequestMapping(value = "/course/{courseId}/quiz/stop", method = RequestMethod.GET)
	@ApiOperation(value = "教师请求此API以关闭学生端随堂测试入口", httpMethod = "GET")
	@ApiResponses(
			@ApiResponse(code = 200, message = "成功情况：{\n" +
					"\"status\": \"SUCCESS\",\n" +
					"\"message\": \"成功关闭随堂测试！\"\n" +
					"}\n失败情况：{\n" +
					"\"status\": \"FAILED\",\n" +
					"\"message\": \"课程 id 非法！\"\n" +
					"}")
	)
	public JsonResponse stopQuiz(@PathVariable Long courseId) throws Http400BadRequestException {
		Course course = courseService.findById(courseId);
		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
		course.setQuizEnable(false);
		courseService.save(course);
		return new JsonResponse(Const.SUCCESS, "成功关闭随堂测试！");
	}

	@RequestMapping(value = "/course/{courseId}/quiz", method = RequestMethod.GET)
	@ApiOperation(value = "获取随堂测试题目，默认全部获取", httpMethod = "GET")
	@ApiResponses(
			@ApiResponse(code = 200, message = "成功请求：\n{\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"quizList\":[\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\": 3,\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\": \"题目：请从一二三四中随机选择一个数字\\t选项：A. 一B. 二C. 三D. 四\",\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"answer\": \"C\"\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\": 4,\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\": \"题目：Java 中类的访问权限有几种\\t选项：A. 4种B. 3种C. 2种D. 1种\",\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"answer\": \"A\"\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;],\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"SUCCESS\"\n" +
					"}\n请求失败，一般是教师端尚未开启接口：\n{\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"尚未开始随堂测试，请等待教师开始后再尝试！\"\n" +
					"}")
	)
	public Map getQuizs(@PathVariable Long courseId) throws Http400BadRequestException {
		Course course = courseService.findById(courseId);
		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
		Map<String, Object> map = new HashMap<>();
		if (course.isQuizEnable()) {
			map.put("status", Const.SUCCESS);
			map.put("quizList", course.getQuizList());
		} else {
			throw new Http400BadRequestException("尚未开始随堂测试，请等待教师开始后再尝试！");
		}
		return map;
	}

	@RequestMapping(value = "/course/{courseId}/quiz/{quizId}", method = RequestMethod.POST)
	@ApiOperation(value = "提交某一测试题的答案", httpMethod = "POST")
	@ApiResponses(
			@ApiResponse(code = 200, message = "提交成功：\n{\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"SUCCESS\",\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"成功提交结果！\"\n" +
					"}\n提交失败（课程id非法）：\n{\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"课程 id 非法！\"\n" +
					"}\n提交失败（测试题id非法）：\n{\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"测试题 id 非法！\"\n" +
					"}\n提交失败（提交入口已关闭）：\n{\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"随堂测试题提交入口已关闭！\"\n" +
					"}\n提交失败（重复提交）：\n{\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"status\": \"FAILED\",\n" +
					"&nbsp;&nbsp;&nbsp;&nbsp;\"message\": \"不能重复提交答案！\"\n" +
					"}")
	)
	public JsonResponse postQuizAnswer(@PathVariable Long courseId, @PathVariable Long quizId, @ApiIgnore @CurrentUser User user, @RequestBody UserQuizResult postResult) throws Http400BadRequestException {
		Course course = courseService.findById(courseId);
		AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("课程 id 非法！"));
		Quiz quiz = quizService.findById(quizId);
		AssertUtils.assertNotNUllElseThrow(quiz, () -> new Http400BadRequestException("测试题 id 非法！"));

		if (course.isQuizEnable()) {
			UserQuizResult result = userQuizResultService.findByUserAndQuiz(user, quiz);
			Map<String, Object> response = new HashMap<>();
			if (result == null) {
				result = new UserQuizResult(postResult.getAnswer(), quiz.getAnswer(), user, quiz);
				userQuizResultService.save(result);
				return new JsonResponse(Const.SUCCESS, "成功提交结果！");
			} else {
				throw new Http400BadRequestException("不能重复提交答案！");
			}
		} else {
			throw new Http400BadRequestException("随堂测试题提交入口已关闭！");
		}
	}
}
