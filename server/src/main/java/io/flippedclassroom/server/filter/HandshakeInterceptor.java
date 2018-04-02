package io.flippedclassroom.server.filter;

import io.flippedclassroom.server.util.LogUtils;
import org.slf4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
	private Logger logger = LogUtils.getInstance();

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		logger.info("开始 WebSocket 握手！");
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
		logger.info("获取 Token:" + req.getServletRequest().getParameter("token"));
		return super.beforeHandshake(request, response, wsHandler, attributes);
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
		logger.info("WebSocket 握手完成！");
		super.afterHandshake(request, response, wsHandler, ex);
	}
}
