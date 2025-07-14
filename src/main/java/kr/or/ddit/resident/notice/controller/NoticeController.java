package kr.or.ddit.resident.notice.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.resident.notice.service.NoticeService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.NoticeVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UnitResidentService unitResidentService;

    @Autowired
    private CommonCodeService commonCodeService;
    
    @GetMapping("/notice")
    public String readNoticeList(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String bldgIdParam,
            SimpleSearch simpleSearch,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

    	log.info("🔎 noticeType = {}", simpleSearch.getNoticeType());

        // 1) 입주민의 유닛 정보 확인
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
        log.info("▶ units 반환 개수 = {}", units == null ? "null" : units.size());
        
        if (units == null || units.isEmpty()) {
            return "redirect:/member/register";
        }

        log.info("📌 bldgIdParam: {}", bldgIdParam);
        log.info("📌 simpleSearch.bldgId (before): {}", simpleSearch.getBldgId());

        // 2) 건물 선택이 안 됐으면 빈 리스트 리턴
        String selectedBldgId = bldgIdParam;
        if(selectedBldgId == null || selectedBldgId.isBlank()) {
        	selectedBldgId = units.stream()
        		.min(Comparator.comparing(UnitResidentVO::getMoveInDt))
        		.map(UnitResidentVO::getBldgId)
        		.orElse(units.get(0).getBldgId());
        }

        // 3) 선택된 건물이 있으면 VO에 세팅
        simpleSearch.setBldgId(selectedBldgId);
        simpleSearch.setBrdCode("N0001");
        log.info("📌 simpleSearch.bldgId (after): {}", simpleSearch.getBldgId());
        
        List<CommonCodeVO> noticeTypeList = commonCodeService.readCommonCodeList("NOTPE");
        model.addAttribute("noticeTypeList", noticeTypeList);
        
        // 4) 페이징 및 검색 수행
        PaginationInfo<NoticeVO> paging = new PaginationInfo<>();
        paging.setCurrentPageNo(page);
        paging.setSimpleSearch(simpleSearch);

        int totalRecord = noticeService.getTotalNoticeCount(paging);
        paging.setTotalRecordCount(totalRecord);

        List<NoticeVO> boardList = noticeService.getNoticeList(paging);
        List<String> residentBldgIds = units.stream()
        			.map(UnitResidentVO::getBldgId)
        			.distinct()
        			.toList();
        final String targetBldgId = selectedBldgId;
        List<NoticeVO> filteredList = boardList.stream()
        		  .filter(notice -> {
        		    if (notice.getBldgId() == null) {
        		      List<String> ownerBldgIds = unitResidentService.getUnitsByMember(notice.getMbrCd())
        		                                        .stream()
        		                                        .map(UnitResidentVO::getBldgId)
        		                                        .distinct()
        		                                        .toList();
        		      log.info("📌 전체공지 판단: noticeNo={}, 등록자={}, 등록자건물={}, 입주민건물={}",
        		                notice.getNoticeNo(), notice.getMbrCd(), ownerBldgIds, residentBldgIds);
        		      return ownerBldgIds.contains(targetBldgId);
        		    } else {
        		      return residentBldgIds.contains(notice.getBldgId());
        		    }
        		  })
        		  .distinct()
        		  .toList();
        
        String pagingHTML = new DefaultPaginationRenderer()
                                 .renderPagination(paging, "fnPaging");
        
        // 5) 모델에 데이터 바인딩
        model.addAttribute("unitList", units);
        model.addAttribute("selectedBldgId", selectedBldgId);
        model.addAttribute("boardList", filteredList);
        model.addAttribute("pagingHTML", pagingHTML);
        model.addAttribute("pagingInfo", paging);

        log.info("🔎 simpleSearch : noticeType={}, searchType={}, searchWord={}",
                simpleSearch.getNoticeType(),
                simpleSearch.getSearchType(),
                simpleSearch.getSearchWord());

        
        return "resident/notice/Notice";
    }
    
}
