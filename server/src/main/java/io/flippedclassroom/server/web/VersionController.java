package io.flippedclassroom.server.web;

import io.flippedclassroom.server.config.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 版本信息
 */
@Api(tags = "版本")
@RestController
public class VersionController {
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	@ApiOperation(value = "版本信息", httpMethod = "GET")
	@ApiResponses({
			@ApiResponse(code = 200, message = "当前版本")
	})
	public String version() {
		return Constant.version;
	}
}
