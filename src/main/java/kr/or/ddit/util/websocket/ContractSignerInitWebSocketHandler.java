package kr.or.ddit.util.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ContractSignerInitWebSocketHandler extends TextWebSocketHandler {

	private final Map<String, Set<WebSocketSession>> initSessions = new ConcurrentHashMap<>();
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
	    String query = session.getUri().getQuery(); // ✅ 이게 null일 수 있음
	    if (query == null || !query.contains("contId=")) {
	        log.warn("[INIT_WS] 잘못된 접속 요청");
	        try {
	            session.close(CloseStatus.BAD_DATA); // ❌ 바로 종료
	        } catch (Exception ignored) {}
	        return;
	    }

	    try {
	        Map<String, String> queryMap = Arrays.stream(query.split("&"))
	            .map(s -> s.split("="))
	            .filter(arr -> arr.length == 2)
	            .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));

	        String contId = queryMap.getOrDefault("contId", null);
	        String role = queryMap.getOrDefault("role", null);

	        // ✅ contId/role 둘 중 하나라도 null이면 종료시키는 걸로 보완
	        if (contId == null || role == null) {
	            log.warn("[INIT_WS] contId 또는 role 누락");
	            session.close(CloseStatus.BAD_DATA);
	            return;
	        }

	        initSessions.computeIfAbsent(contId, k -> new CopyOnWriteArraySet<>()).add(session);
	        session.getAttributes().put("contId", contId);
	        session.getAttributes().put("role", role);

	        log.info("[INIT_WS] 연결 성공: contId={}, role={}, session={}", contId, role, session.getId());

	    } catch (Exception e) {
	        log.error("[INIT_WS] 접속 처리 중 오류:", e);
	    }
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) {
		String contId = (String) session.getAttributes().get("contId");
		String senderRole = (String) session.getAttributes().get("role");

		if (contId == null || senderRole == null) {
			log.warn("[INIT_WS] 세션 정보 누락");
			return;
		}

		String raw = message.getPayload();
		Map<String, Object> parsed;
		try {
			parsed = mapper.readValue(raw, Map.class);
		} catch (Exception e) {
			log.warn("[INIT_WS] JSON 파싱 실패: {}", raw);
			return;
		}

		String type = (String) parsed.get("type");
		String msgContId = (String) parsed.get("contId");
		Object payload = parsed.get("payload");

		if (!"INIT_REQUEST".equals(type) || !contId.equals(msgContId) || payload == null) {
			log.warn("[INIT_WS] 무시된 메시지: {}", raw);
			return;
		}

		for (WebSocketSession s : initSessions.getOrDefault(contId, Set.of())) {
			String targetRole = (String) s.getAttributes().get("role");

			// 동일인이 아닌 경우만 전파
			if (!senderRole.equals(targetRole) && s.isOpen()) {
				try {
				    Map<String, Object> response = new HashMap<>();
				    response.put("type", "INIT_RESPONSE");
				    response.put("contId", contId);
				    response.put("role", targetRole);

				    // ✅ 항상 배열로 래핑
				    List<Object> wrappedPayload = new ArrayList<>();
				    if (payload instanceof List) {
				        wrappedPayload = (List<Object>) payload;
				    } else {
				        wrappedPayload.add(payload);
				    }

				    response.put("payload", wrappedPayload);

				    s.sendMessage(new TextMessage(mapper.writeValueAsString(response)));
				    log.info("[INIT_WS] INIT_RESPONSE 전송: to={}, contId={}", targetRole, contId);

				} catch (Exception e) {
				    log.error("[INIT_WS] 전송 실패: {}", e.getMessage());
				}
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		String contId = (String) session.getAttributes().get("contId");
		if (contId != null) {
			Set<WebSocketSession> sessions = initSessions.get(contId);
			if (sessions != null) {
				sessions.remove(session);
				if (sessions.isEmpty()) {
					initSessions.remove(contId);
				}
			}
		}
		log.info("[INIT_WS] 종료: contId={}, session={}", contId, session.getId());
	}
}
