package io.flippedclassroom.server.web;

import io.flippedclassroom.server.config.Constant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 版本信息
 */
@RestController
public class VersionController {
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public String version() {
		return Constant.version;
	}
}
