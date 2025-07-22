package kr.or.ddit.util.websocket;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@DependsOn("schedulerFactoryBean")
public class ContractExpireWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("🟢 계약 만료 WebSocket 연결됨: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("🔴 계약 만료 WebSocket 종료됨: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 필요시 클라이언트에서 ping 보내는 용도 등으로 활용
    }

    public void broadcastExpiredContract(String contId) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage("EXPIRED:" + contId));
                } catch (Exception e) {
                    log.error("❌ 전송 실패 - sessionId: {}, error: {}", session.getId(), e.getMessage());
                }
            }
        }
    }
}
