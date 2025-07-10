package kr.or.ddit.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import kr.or.ddit.util.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/chat");
    }
}