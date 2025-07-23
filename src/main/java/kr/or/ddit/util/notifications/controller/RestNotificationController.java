package kr.or.ddit.util.notifications.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.broker.mapper.BrokerAuthUnpackingMapper;
import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.notifications.service.NotificationsService;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.NotificationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/notifications")
@RequiredArgsConstructor
public class RestNotificationController {

    private final NotificationsService service;
    private final BrokerAuthUnpackingService authUnpack;
    private final AES256Util aes256Util;
    private final ObjectMapper objectMapper;

    @SuppressWarnings("finally")
	@PostMapping("/loading")
    public Map<String, String> getNotifications(
    		Principal principal
    		, @RequestBody String payload
    ) {
		
//		/** DEBUGGING이다 씨밤바라ㅗ^ㅂ^ㅗ **/
//		NotificationVO vo = new NotificationVO();
//		vo.setMbrCd("M2507000110");
//		vo.setNotifDelYn(false);
//		vo.setNotifDt(String.valueOf(LocalDateTime.now()));
//		vo.setNotifMsg("테스트용으로 작성한다 씨밤바라 ^0^");
//		vo.setNotifReadYn(false);
//		vo.setNotifRefUrl("/");
//		vo.setNotifTitle("제목이다 무엇의? 알림의 ^ㅂ^");
//		vo.setNotifTypeCd("001");
//		vo.setNotifTypeGroupCd("NTFS");
		try {
//			service.createNotificationApprove(vo);
		} catch (Exception e) {
			log.debug("테스트 알림 생성이 뭐가 잘 안 됐다 씨밤바라 ㅗㅗ^ㅂ^ㅗㅗ");
			e.printStackTrace();
		} finally {
			String username = principal.getName();
			List<NotificationVO> notifications = service.readNotificationListWithoutPaging(authUnpack.getMbrCd(username));
			String resultJson = "";
			try {
				resultJson = objectMapper.writeValueAsString(notifications);
				log.debug("제대로 파싱했다 씨밤바라^ㅂ^ㅗ : {}",resultJson);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			Map<String, String> resultMap = aes256Util.encryptWithDynamicIV(resultJson);
			log.debug("제대로 암호화했다 씨밤바라^ㅂ^ㅗㅗ : {}", resultMap);
			
			return resultMap;
		}		
    }
    
    @PostMapping("/read/{notifId}")
    public Map<String, String> readAndRedirect(@PathVariable("notifId") String notifId) {
    	NotificationVO notif = service.readAndReturn(notifId);
    	String resultJson = "";
    	try {
			resultJson = objectMapper.writeValueAsString(notif);
			log.debug("REST 단건 알림 클릭 --> 조회 {}", resultJson);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	Map<String, String> resultMap = aes256Util.encryptWithDynamicIV(resultJson);
		log.debug("제대로 암호화했다 씨밤바라^ㅂ^ㅗㅗ : {}", resultMap);
		
		return resultMap;
    }
//
//    @PostMapping("/read/{notifId}")
//    public ResponseEntity<Void> readNotification(@PathVariable("notifId") String notifId) {
//        service.readAndReturn(notifId);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{notifId}")
//    public ResponseEntity<Void> deleteNotification(@PathVariable("notifId") String notifId) {
//        service.removeNotification(notifId); // 이 메서드는 구현되어야 함
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/count")
//    public ResponseEntity<Long> getUnreadCount(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
//        MemberVO member = principal.getRealUser();
//        long count = service.readNotificationList(member.getMbrCd()).stream()
//                .filter(n -> !n.isNotifReadYn())
//                .count();
//        return ResponseEntity.ok(count);
//    }
}
