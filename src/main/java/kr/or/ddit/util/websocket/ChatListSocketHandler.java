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
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        if ("ping".equalsIgnoreCase(payload)) {
            session.sendMessage(new TextMessage("pong"));
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        chatListSessions.remove(session);
    }

    public void broadcastMessageUpdate(String roomId, String mbrNnm, String rcmCont, String unitRoom, String rcmTime ) {
        Map<String, Object> payload = Map.of(
            "type", "messagePreview",
            "residentChatRoomId", roomId,
            "rcmCont", rcmCont,           
            "rcmTime", rcmTime, 
            "unitRoom", unitRoom,
            "mbrNnm", mbrNnm              
        );

        sendToAll(payload);
       
    }

    public void broadcastNewChatRoom(Map<String, Object> newRoomInfo) {
        newRoomInfo.put("type", "newRoom");
        sendToAll(newRoomInfo);

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

        }
    }
}
