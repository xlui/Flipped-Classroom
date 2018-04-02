package io.flippedclassroom.server.config;

import io.flippedclassroom.server.filter.HandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
		stompEndpointRegistry
				.addEndpoint(Const.webSocketEndpoint)
				.setAllowedOrigins("*")     // 解决跨域问题
                .addInterceptors(new HandshakeInterceptor())
                .withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker(Const.broadcast, Const.group, Const.user);
		registry.setUserDestinationPrefix(Const.user);
	}
}
