package kr.or.ddit.resident.notice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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

    @Autowired private CommonCodeService   commonCodeService;
    @Autowired private UnitResidentService unitResidentService;
    @Autowired private NoticeService       noticeService;

    @PreAuthorize("hasAnyRole('ADMIN','TENANCY')")
    @GetMapping("/form")
    public String showForm(
            @RequestParam(value="noticeNo",    required=false) String noticeNo,
            @RequestParam(value="bldgIdParam", required=false) String bldgIdParam,
            @RequestParam(value="page",        defaultValue="1") String page,
            @RequestParam(value="noticeType",  defaultValue="")  String noticeType,
            @RequestParam(value="searchType",  defaultValue="")  String searchType,
            @RequestParam(value="searchWord",  defaultValue="")  String searchWord,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            Model model
    ) {
        String mbrCd = principal.getRealUser().getMbrCd();

        // 1) 건물 목록
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(mbrCd);
        List<String> bldgList = units.stream()
                                     .map(UnitResidentVO::getBldgId)
                                     .distinct()
                                     .toList();
        // 2) 공지유형 목록
        List<CommonCodeVO> noticeTypeList = commonCodeService.readCommonCodeList("NOTPE");

        // 3) 수정 모드라면 권한 체크 후 기존 글 조회
        NoticeVO notice;
        if (StringUtils.hasText(noticeNo)) {
            notice = noticeService.getNoticeById(noticeNo);
            boolean isAdmin  = principal.getRealUser().getMemRoleList()
                                         .stream()
                                         .anyMatch(r->"ADMIN".equals(r.getUserRoleId()));
            boolean isAuthor = mbrCd.equals(notice.getMbrCd());
            if(!isAdmin && !isAuthor) {
                return "redirect:/resident/notice/denied";
            }
        } else {
            notice = new NoticeVO();
        }

        // 4) 모델 바인딩
        model.addAttribute("notice",         notice);
        model.addAttribute("unitList",       units);
        model.addAttribute("bldgList",       bldgList);
        model.addAttribute("noticeTypeList", noticeTypeList);
        model.addAttribute("bldgIdParam",    bldgIdParam);
        model.addAttribute("page",           page);
        model.addAttribute("noticeType",     noticeType);
        model.addAttribute("searchType",     searchType);
        model.addAttribute("searchWord",     searchWord);

        return "resident/notice/NoticeForm";
    }

    @PreAuthorize("hasAnyRole('ADMIN','TENANCY')")
    @PostMapping("/form")
    public String saveOrUpdate(
            @ModelAttribute NoticeVO notice,
            @RequestParam("bldgIdHidden")     String bldgIdParam,
            @RequestParam(value="page",       defaultValue="1")  String page,
            @RequestParam(value="noticeType", defaultValue="")   String noticeType,
            @RequestParam(value="searchType", defaultValue="")   String searchType,
            @RequestParam(value="searchWord", defaultValue="")   String searchWord,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            RedirectAttributes redirectAttributes
    ) {
        String mbrCd = principal.getRealUser().getMbrCd();
        boolean isAdmin = principal.getRealUser().getMemRoleList()
                                   .stream()
                                   .anyMatch(r->"ADMIN".equals(r.getUserRoleId()));

        // --- 수정 모드 ---
        if (StringUtils.hasText(notice.getNoticeNo())) {
            NoticeVO existing = noticeService.getNoticeById(notice.getNoticeNo());
            boolean isAuthor = mbrCd.equals(existing.getMbrCd());
            if (!isAdmin && !isAuthor) {
                return "redirect:/resident/notice/denied";
            }

            existing.setBldgId("ALL".equalsIgnoreCase(bldgIdParam) ? null : bldgIdParam);
            existing.setNoticeType(notice.getNoticeType());
            existing.setBrdTitlNm(notice.getBrdTitlNm());
            existing.setBrdCont(notice.getBrdCont());

            noticeService.updateNotice(existing);

            noticeService.updateBoardBuilding(existing.getNoticeNo(), existing.getBldgId());
            
            noticeService.updateBoardContent(existing);

            
            log.info("🔧 선택된 bldgIdHidden = {}", bldgIdParam);
            log.info("✏️ 수정모드: noticeNo={}, 기존 bldgId={}, 새로 설정된 bldgId={}", 
                    existing.getNoticeNo(), existing.getBldgId(), 
                    "ALL".equalsIgnoreCase(bldgIdParam) ? null : bldgIdParam);
            log.info("✏️ 제목 = {}", notice.getBrdTitlNm());
            log.info("✏️ 내용 = {}", notice.getBrdCont());
            
            // 변경된 건물 값으로 상세페이지 이동
            String targetBldg = "ALL".equalsIgnoreCase(bldgIdParam) ? "ALL" : bldgIdParam;
            redirectAttributes.addFlashAttribute("success", "공지 수정이 완료되었습니다.");
            redirectAttributes.addAttribute("noticeNo",    existing.getNoticeNo());
            redirectAttributes.addAttribute("bldgIdParam", targetBldg);
            redirectAttributes.addAttribute("page",        page);
            redirectAttributes.addAttribute("noticeType",  noticeType);
            redirectAttributes.addAttribute("searchType",  searchType);
            redirectAttributes.addAttribute("searchWord",  searchWord);

            return "redirect:/resident/notice/detail";
        }
        
        if (!StringUtils.hasText(bldgIdParam)) {
        	  redirectAttributes.addFlashAttribute("error", "건물을 선택하거나 전체 공지로 설정해야 합니다.");
        	  return "redirect:/resident/notice/form";
        	}

        	if (!StringUtils.hasText(notice.getNoticeType())) {
        	  redirectAttributes.addFlashAttribute("error", "공지 유형을 선택해야 합니다.");
        	  return "redirect:/resident/notice/form";
        	}
        
        // --- 등록 모드 ---
        notice.setMbrCd(mbrCd);
        notice.setBldgId("ALL".equalsIgnoreCase(bldgIdParam) ? null : bldgIdParam);
        noticeService.registerNotice(notice);

        redirectAttributes.addFlashAttribute("success", "공지 등록이 완료되었습니다.");
        redirectAttributes.addAttribute("bldgIdParam", bldgIdParam);
        redirectAttributes.addAttribute("searchWord",  searchWord);

        return "redirect:/resident/notice";
    }
}
