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
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionList.add(session);
        log.info("✅ 연결: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessageVO msg = new ObjectMapper().readValue(message.getPayload(), ChatMessageVO.class);
        service.createMessage(msg); // 메시지 저장

        for (WebSocketSession s : sessionList) {
            if (s.isOpen()) s.sendMessage(message); // 전송
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionList.remove(session);
        log.info("⛔ 종료: {}", session.getId());
    }
}