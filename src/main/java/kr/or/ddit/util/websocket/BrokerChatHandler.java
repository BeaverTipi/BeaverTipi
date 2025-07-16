package kr.or.ddit.util.websocket;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.main.brokerChat.service.BrokerChatService;
import kr.or.ddit.vo.ChatMessageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class BrokerChatHandler extends TextWebSocketHandler {

    private final BrokerChatService service;
    private final List<WebSocketSession> sessionList = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //  URI의 queryString 추출
        String query = session.getUri().getQuery(); // 예: crId=CR000123

        String crId = null;

        //  query가 존재하고 crId 파라미터일 경우 → 값 추출
        if (query != null && query.startsWith("crId=")) {
            crId = query.substring("crId=".length());
        }

        //  crId가 유효한 경우 → 세션 속성에 저장 + 세션 등록
        if (crId != null) {
            session.getAttributes().put("crId", crId);
            sessionList.add(session);
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessageVO msg = new ObjectMapper().readValue(message.getPayload(), ChatMessageVO.class);
        service.createMessage(msg); // 메시지 저장

        for (WebSocketSession s : sessionList) {
            // 현재 메시지를 보낸 사용자와 동일한 세션인지 비교
            if (!s.getId().equals(session.getId()) && s.isOpen()) {
                s.sendMessage(message); // 자기 제외한 세션에게만 전송
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionList.remove(session);
        log.info("⛔ 종료: {}", session.getId());
    }
}