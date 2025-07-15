package kr.or.ddit.util.notifications.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.util.notifications.service.NotificationsService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.NotificationVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationsService service;
    private final String MODELNAME = "search";

    @GetMapping("/notification/read/{notifId}")
    public String readNotification(@PathVariable("notifId") String notifId, RedirectAttributes redirectAttributes) {
        // 1. 알림 읽음 처리 (DB 업데이트)
        NotificationVO noti = service.readAndReturn(notifId);

        // 2. 이동할 링크로 리다이렉트 (없으면 메인으로)
        String targetUrl = noti.getNotifRefUrl();
        return "redirect:" + (targetUrl != null ? targetUrl : "/");
    }
    @GetMapping("/ajax/notification/list")
    public String readNotificationList(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        MemberVO member = principal.getRealUser();

        // 페이징 처리
        PaginationInfo<NotificationVO> paging = new PaginationInfo<>();
        paging.setCurrentPageNo(page);
        paging.setDetailSearch(new NotificationVO()); // 조건 없음

        // 전체 알림 수
        int total = service.readTotalNotificationCount(member.getMbrCd());
        paging.setTotalRecordCount(total);

        // 목록 조회
        List<NotificationVO> list = service.readNotificationList(member.getMbrCd(), paging);

        // 페이징 HTML
        String pagingHTML = new DefaultPaginationRenderer().renderPagination(paging, "fnPaging");

        model.addAttribute("notifications", list);
        model.addAttribute("pagingHTML", pagingHTML);

        return "main/member/notification"; // JSP fragment
    }

    	
    

}
