package kr.or.ddit.util.websocket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.resident.chating.service.RsdChatServiceImpl;
import kr.or.ddit.vo.ResidentChatMessageVO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final RsdChatServiceImpl service;

    private final List<WebSocketSession> sessionList = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        String residentChatRoomId = null;

        if (query != null && query.startsWith("residentChatRoomId=")) {
            residentChatRoomId = query.split("=")[1];
        }

        if (residentChatRoomId != null) {
            session.getAttributes().put("residentChatRoomId", residentChatRoomId);
            sessionList.add(session);
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String senderRoomId = (String) session.getAttributes().get("residentChatRoomId");

        ObjectMapper mapper = new ObjectMapper();
        ResidentChatMessageVO msg = mapper.readValue(message.getPayload(), ResidentChatMessageVO.class);

        service.createMessage(msg);

        
        
        for (WebSocketSession s : sessionList) {
            String targetRoomId = (String) s.getAttributes().get("residentChatRoomId");
            if (senderRoomId != null && senderRoomId.equals(targetRoomId)) {
                if (!s.getId().equals(session.getId()) && s.isOpen()) {
                    s.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionList.remove(session);
    }
}