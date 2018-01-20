package io.flippedclassroom.server.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class TestPageController {
	@RequestMapping("/hello")
	public String index() {
		return "Hello World!";
	}
}
