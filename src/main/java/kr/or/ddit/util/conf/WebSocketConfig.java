package kr.or.ddit.util.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import kr.or.ddit.util.websocket.BrokerChatHandler;
import kr.or.ddit.util.websocket.ChatListSocketHandler;
import kr.or.ddit.util.websocket.ContractExpireWebSocketHandler;
import kr.or.ddit.util.websocket.ContractSignerWebSocketHandler;
import kr.or.ddit.util.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	private final BrokerChatHandler brokerChatHandler;
    private final WebSocketHandler webSocketHandler;
    private final ChatListSocketHandler chatListSocketHandler;
    private final ContractExpireWebSocketHandler contractExpireWebSocketHandler; 
    private final ContractSignerWebSocketHandler contractSignerWebSocketHandler;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*"); ;
        registry.addHandler(chatListSocketHandler, "/ws/chatList").setAllowedOrigins("*"); ;
        registry.addHandler(brokerChatHandler, "/ws/brokerChat").setAllowedOrigins("*"); ;
        registry.addHandler(contractExpireWebSocketHandler, "/ws/contractExpire").setAllowedOrigins("*"); // ^0^
        registry.addHandler(contractSignerWebSocketHandler, "/ws/signers").setAllowedOrigins("*"); // ^0^
    }
}