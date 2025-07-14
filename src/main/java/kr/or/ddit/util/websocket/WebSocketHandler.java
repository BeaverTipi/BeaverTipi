package kr.or.ddit.util.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.resident.chating.dto.ChatMessageDTO;
import kr.or.ddit.resident.chating.service.RsdChatServiceImpl;
import kr.or.ddit.vo.ResidentChatMessageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final RsdChatServiceImpl service;
    private final ChatListSocketHandler chatListSocketHandler; // ✅ 목록용 브로드캐스트 핸들러

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

        // ✅ 메시지 저장
        service.createMessage(msg);

        // ✅ 보낸 사람 정보 조회
        ChatMessageDTO senderInfo = service.getWhoIsSender(msg.getMbrCd(), msg.getResidentChatRoomId());

        // ✅ 방 참여자에게 브로드캐스트
        Map<String, Object> broadcastMsg = new HashMap<>();
        broadcastMsg.put("residentChatRoomId", msg.getResidentChatRoomId());
        broadcastMsg.put("mbrCd", msg.getMbrCd());
        broadcastMsg.put("rcmCont", msg.getRcmCont());
        broadcastMsg.put("mbrNnm", senderInfo.getMbrNnm());
        broadcastMsg.put("unitRoom", senderInfo.getUnitRoom());

        String json = mapper.writeValueAsString(broadcastMsg);

        for (WebSocketSession s : sessionList) {
            String targetRoomId = (String) s.getAttributes().get("residentChatRoomId");
            if (senderRoomId != null && senderRoomId.equals(targetRoomId)) {
                if (!s.getId().equals(session.getId()) && s.isOpen()) {
                    s.sendMessage(new TextMessage(json));
                }
            }
        }

        // ✅ 목록 사용자에게도 실시간 프리뷰 갱신 브로드캐스트
        chatListSocketHandler.broadcastMessageUpdate(
        	    msg.getResidentChatRoomId(),
        	    senderInfo.getMbrNnm(),
        	    msg.getRcmCont(),
        	    senderInfo.getUnitRoom() // ✅ 추가됨
        	);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionList.remove(session);

        String roomId = (String) session.getAttributes().get("residentChatRoomId");
        log.info("WebSocket 연결 종료 → sessionId={}, roomId={}, 상태코드={}, 사유={}",
                 session.getId(), roomId, status.getCode(), status.getReason());
    }
}