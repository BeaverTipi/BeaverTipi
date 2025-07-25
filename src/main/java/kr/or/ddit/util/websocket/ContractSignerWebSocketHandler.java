package kr.or.ddit.util.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
public class ContractSignerWebSocketHandler extends TextWebSocketHandler {

    // 계약별 서명자 세션 목록: contId → 세션 Set
    private final Map<String, Set<WebSocketSession>> contractSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = session.getUri().getQuery(); // ?contId=abc123&role=LESSEE
        if (query == null || !query.contains("contId=")) {
            log.warn("❌ 유효하지 않은 WebSocket 접속 시도");
            try {
                session.close(CloseStatus.BAD_DATA);
            } catch (Exception ignored) {}
            return;
        }

        String[] parts = query.split("&");
        String contId = parts[0].split("=")[1];

        contractSessions
            .computeIfAbsent(contId, k -> new CopyOnWriteArraySet<>())
            .add(session);

        session.getAttributes().put("contId", contId);
        log.info("🟢 WebSocket 연결됨: contId={}, session={}", contId, session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String contId = (String) session.getAttributes().get("contId");
        if (contId != null) {
            Set<WebSocketSession> sessions = contractSessions.get(contId);
            if (sessions != null) sessions.remove(session);
            log.info("🔴 WebSocket 종료: contId={}, session={}", contId, session.getId());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String contId = (String) session.getAttributes().get("contId");
        if (contId == null) return;

        // 모든 연결된 사용자에게 메시지 broadcast
        Set<WebSocketSession> sessions = contractSessions.get(contId);
        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    try {
                        s.sendMessage(message);
                    } catch (Exception e) {
                        log.error("❌ WebSocket 메시지 전송 실패: {}", e.getMessage());
                    }
                }
            }
        }
    }

    // 필요 시 외부에서 호출할 수 있는 broadcast 메서드
    public void broadcastToContract(String contId, String message) {
        Set<WebSocketSession> sessions = contractSessions.get(contId);
        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    try {
                        s.sendMessage(new TextMessage(message));
                    } catch (Exception e) {
                        log.error("❌ 외부 broadcast 실패: {}", e.getMessage());
                    }
                }
            }
        }
    }
}
