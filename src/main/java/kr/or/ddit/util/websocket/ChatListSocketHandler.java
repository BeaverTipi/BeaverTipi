package kr.or.ddit.util.websocket;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChatListSocketHandler extends TextWebSocketHandler {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Set<WebSocketSession> chatListSessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        chatListSessions.add(session);
        log.info("🔌 채팅 목록 WebSocket 연결됨 → sessionId={}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        if ("ping".equalsIgnoreCase(payload)) {
            session.sendMessage(new TextMessage("pong"));
        }
        log.info("📩 목록 WebSocket 메시지 수신: {}", payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        chatListSessions.remove(session);
        log.info("❌ 목록 WebSocket 연결 종료 → sessionId={}, 상태={}", session.getId(), status);
    }

    public void broadcastMessageUpdate(String roomId, String sender, String content, String unitRoom) {
        Map<String, Object> payload = Map.of(
            "type", "messagePreview",
            "residentChatRoomId", roomId,
            "sender", sender,
            "content", content,
            "unitRoom", unitRoom // ✅ 새로 추가됨
        );

        sendToAll(payload);
        log.info("🚀 메시지 프리뷰 갱신 브로드캐스트 완료 → roomId={}", roomId);
    }

    public void broadcastNewChatRoom(Map<String, Object> newRoomInfo) {
        newRoomInfo.put("type", "newRoom");
        sendToAll(newRoomInfo);
        log.info("📡 새 채팅방 생성 정보 브로드캐스트 완료");
    }

    private void sendToAll(Map<String, Object> payload) {
        try {
            String json = mapper.writeValueAsString(payload);
            for (WebSocketSession session : chatListSessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (Exception e) {
            log.error("❌ WebSocket 목록 브로드캐스트 실패:", e);
        }
    }
}
