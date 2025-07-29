package kr.or.ddit.util.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ContractSignerWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, Set<WebSocketSession>> contractSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String contId = getQueryParam(session, "contId");
        String role = getQueryParam(session, "role");

        contractSessions.computeIfAbsent(contId, k -> Collections.newSetFromMap(new ConcurrentHashMap<>())).add(session);
        session.getAttributes().put("contId", contId);

        log.info("[REALTIME_WS] 연결됨: contId={}, role={}, session={}", contId, role, session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String raw = message.getPayload();
        String[] parts = raw.split(":", 4);

        if (parts.length < 4) {
            log.warn("[REALTIME_WS] 잘못된 메시지 포맷: {}", raw);
            return;
        }

        String type = parts[0];
        String contId = parts[1];
        String role = parts[2];
        String payload = parts[3];

        log.debug("[REALTIME_WS] 수신 → type={}, contId={}, role={}, payload={}", type, contId, role, payload);

        // 같은 계약 ID를 가진 모든 세션에 메시지 브로드캐스트
        broadcast(contId, raw);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String contId = (String) session.getAttributes().get("contId");
        if (contId != null) {
            contractSessions.getOrDefault(contId, Collections.emptySet()).remove(session);
            log.info("[REALTIME_WS] 연결 종료: contId={}, session={}", contId, session.getId());
        }
    }

    private void broadcast(String contId, String message) throws Exception {
        for (WebSocketSession s : contractSessions.getOrDefault(contId, Collections.emptySet())) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(message));
            }
        }
    }

    private String getQueryParam(WebSocketSession session, String key) {
        return Arrays.stream(Objects.requireNonNull(session.getUri()).getQuery().split("&"))
                .filter(param -> param.startsWith(key + "="))
                .map(param -> param.split("=")[1])
                .findFirst()
                .orElse(null);
    }
}
