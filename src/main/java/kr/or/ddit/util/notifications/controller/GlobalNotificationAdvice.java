package kr.or.ddit.util.notifications.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import kr.or.ddit.util.notifications.service.NotificationsService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.NotificationVO;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalNotificationAdvice {

    private final NotificationsService notificationsService;

    @ModelAttribute("notifications")
    public List<NotificationVO> notifications(Authentication auth, @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
    	if (auth == null || !(auth.getPrincipal() instanceof RealUserWrapper)) {
    		return Collections.emptyList();
        }
    	MemberVO member = principal.getRealUser();
    	return notificationsService.readNotificationList(member.getMbrCd());
    }

    @ModelAttribute("unreadCount")
    public long unreadCount(@ModelAttribute("notifications") List<NotificationVO> notifications) {
        return notifications.stream()
        		.filter(n -> !n.isNotifReadYn())
        		.count();
    }
}

