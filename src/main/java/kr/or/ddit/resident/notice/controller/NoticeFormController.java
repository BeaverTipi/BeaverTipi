package kr.or.ddit.resident.notice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.resident.notice.service.NoticeService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.NoticeVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident/notice")
public class NoticeFormController {

    @Autowired
    private CommonCodeService commonCodeService;

    @Autowired
    private UnitResidentService unitResidentService;

    @Autowired
    private NoticeService noticeService;

    // 등록 폼—임대인/관리자 전용
    @PreAuthorize("hasAnyRole('ADMIN','TENANCY')")
    @GetMapping("/form")
    public String showForm(
            Model model,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            @RequestParam(value = "bldgIdParam", required = false) String bldgIdParam
    ) {
        String mbrCd = principal.getRealUser().getMbrCd();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(mbrCd);
        
        model.addAttribute("selectedBldgId", bldgIdParam);
        model.addAttribute("unitList", units);
        model.addAttribute("noticeTypeList", 
            commonCodeService.readCommonCodeList("NOTPE"));
        model.addAttribute("notice", new NoticeVO());
        return "resident/notice/NoticeForm";
    }

    // 등록 처리—임대인/관리자 전용
    @PreAuthorize("hasAnyRole('ADMIN','TENANCY')")
    @PostMapping("/form")
    public String create(
            @ModelAttribute NoticeVO notice,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            @RequestParam("bldgIdHidden") String bldgIdParam,
            RedirectAttributes redirectAttributes
    ) {
    	String mbrCd = principal.getRealUser().getMbrCd();
        notice.setMbrCd(mbrCd);

        boolean isAllNotice = "ALL".equalsIgnoreCase(bldgIdParam);

        // 🔍 전체 공지 체크 여부에 따라 건물 ID 처리
        if (isAllNotice) {
            notice.setBldgId(null); // 전체 공지일 경우 건물 ID 제거
        } else {
            notice.setBldgId(bldgIdParam);
        }
        

        noticeService.registerNotice(notice);

        // ✅ 등록 완료 메시지 전달
        redirectAttributes.addFlashAttribute("success", "공지 등록이 완료되었습니다.");

        // 🔁 등록 후 건물 기준 유지하여 리스트 복귀
        return "redirect:/resident/notice" + (isAllNotice ? "" : "?bldgIdParam=" + bldgIdParam);
    }
    

}