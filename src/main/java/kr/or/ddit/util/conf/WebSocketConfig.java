package kr.or.ddit.util.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import kr.or.ddit.util.websocket.BrokerChatHandler;
import kr.or.ddit.util.websocket.ChatListSocketHandler;
import kr.or.ddit.util.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	private final BrokerChatHandler brokerChatHandler;
    private final WebSocketHandler webSocketHandler;
    private final ChatListSocketHandler chatListSocketHandler;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/chat");
        registry.addHandler(chatListSocketHandler, "/ws/chatList");
        registry.addHandler(brokerChatHandler, "/ws/brokerChat");
    }
}