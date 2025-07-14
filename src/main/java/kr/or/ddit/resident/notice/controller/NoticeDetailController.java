package kr.or.ddit.resident.notice.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.notice.service.NoticeService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.NoticeVO;
import kr.or.ddit.vo.RoleAchievedVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident/notice")
public class NoticeDetailController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UnitResidentService unitResidentService;

    /**
     * 공지사항 상세 조회
     */
    @GetMapping("/detail")
    public String detail(
            @RequestParam String noticeNo,
            @RequestParam String bldgIdParam,
            @RequestParam(defaultValue = "") String noticeType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String searchType,
            @RequestParam(defaultValue = "") String searchWord,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            Model model
    ) {
        // 조회수 증가
        NoticeVO voForCount = new NoticeVO();
        voForCount.setNoticeNo(noticeNo);
        noticeService.viewCount(voForCount);

        // 상세 데이터 조회
        NoticeVO detail = noticeService.getNoticeById(noticeNo);

        // 게시일자 포맷 변환
        LocalDateTime publishedAt = detail.getBrdPblsDtm();
        Date convertedDate = Date.from(
            publishedAt.atZone(ZoneId.systemDefault()).toInstant()
        );
        model.addAttribute("convertedDate", convertedDate);

        // 기본 전달값
        model.addAttribute("notice",       detail);
        model.addAttribute("bldgIdParam",  bldgIdParam);
        model.addAttribute("noticeType",   noticeType);
        model.addAttribute("page",         page);
        model.addAttribute("searchType",   searchType);
        model.addAttribute("searchWord",   searchWord);

        // 로그인 사용자 정보
        MemberVO user = principal.getRealUser();
        model.addAttribute("memRoleList",  user.getMemRoleList());
        model.addAttribute("mbrCd",        user.getMbrCd());

        return "resident/notice/NoticeDetail";
    }

    /**
     * 공지 삭제 처리
     */
    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN','TENANCY')")
    public String deleteNotice(
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            @RequestParam String noticeNo,
            @RequestParam String bldgIdParam,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String noticeType,
            @RequestParam(defaultValue = "") String searchType,
            @RequestParam(defaultValue = "") String searchWord
    ) {
        MemberVO user = principal.getRealUser();
        NoticeVO notice = noticeService.getNoticeById(noticeNo);

        boolean isAdmin = user.getMemRoleList().stream()
                              .map(RoleAchievedVO::getUserRoleId)
                              .anyMatch("ADMIN"::equals);
        boolean isAuthor = notice.getMbrCd().equals(user.getMbrCd());

        if (!isAdmin && !isAuthor) {
            log.warn("❌ 삭제 권한 없음: 사용자={}, 작성자={}", user.getMbrCd(), notice.getMbrCd());
            return "redirect:/resident/notice/denied";
        }

        noticeService.softDeleteNotice(noticeNo);

        // 삭제 후 목록으로 복귀
        return "redirect:/resident/notice"
             + "?bldgIdParam=" + bldgIdParam
             + "&page="        + page
             + "&noticeType="  + noticeType
             + "&searchType="  + searchType
             + "&searchWord="  + searchWord;
    }
}