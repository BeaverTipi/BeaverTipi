package kr.or.ddit.util.websocket;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
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
        //모든 WebSocket 메시지는 아래의 4파트 구조를 따른다.
    	//<TYPE>:<contId>:<role>:<payload (JSON)>
    	String query = session.getUri().getQuery();
        if (query == null || !query.contains("contId=")) {
            log.warn("유효하지 않은 WebSocket 접속 시도");
            try {
                session.close(CloseStatus.BAD_DATA);
            } catch (Exception ignored) {}
            return;
        }

        try {
            String[] parts = query.split("&");
            String contId = parts[0].split("=")[1];
            String role = parts.length > 1 ? parts[1].split("=")[1] : "UNKNOWN";

            contractSessions
                .computeIfAbsent(contId, k -> new CopyOnWriteArraySet<>())
                .add(session);
            session.getAttributes().put("contId", contId);

            log.info("----<><>WebSocket 연결 성공: contId={}, role={}, session={}", contId, role, session.getId());

            // JOINED 메시지 전파
            ObjectMapper mapper = new ObjectMapper();
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("contId", contId);
            payload.put("role", role);
            payload.put("status", "JOINED");
            payload.put("connected", true);
            payload.put("signedAt", null);
            payload.put("isRejected", false);

            Map<String, Object> fullMessage = new HashMap<>();
            fullMessage.put("type", "JOINED");
            fullMessage.put("contId", contId);
            fullMessage.put("role", role);
            fullMessage.put("payload", payload);

            String webSocketMessage = mapper.writeValueAsString(fullMessage);
            
            for (WebSocketSession s : contractSessions.get(contId)) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(webSocketMessage));
                }
            }
        } catch (Exception e) {
            log.error("--<><>WebSocket 접속 처리 중 오류:", e.getMessage());
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String contId = (String) session.getAttributes().get("contId");
        if (contId != null) {
            Set<WebSocketSession> sessions = contractSessions.get(contId);
            if (sessions != null) sessions.remove(session);
            log.info("--<><>WebSocket 종료: contId={}, session={}", contId, session.getId());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String contId = (String) session.getAttributes().get("contId");
        if (contId == null) return;

        String msgPayload = message.getPayload();
        if (!msgPayload.trim().startsWith("{")) {
            log.warn("무시된 WebSocket 문자열 메시지: {}", msgPayload);
            return;
        }

        for (WebSocketSession s : contractSessions.get(contId)) {
            if (s.isOpen() && !s.getId().equals(session.getId())) {
                try {
                    s.sendMessage(message);
                } catch (Exception e) {
                    log.error("--<><>WebSocket 메시지 전송 실패: {}", e.getMessage());
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
                        log.error("--<><>외부 broadcast 실패: {}", e.getMessage());
                    }
                }
            }
        }
    }
}
