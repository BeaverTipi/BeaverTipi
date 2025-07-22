package kr.or.ddit.resident.notice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.resident.notice.service.NoticeService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.NoticeVO;
import kr.or.ddit.vo.UnitResidentVO;

@RestController
@RequestMapping("/ajax/resident/api/notice")
public class NoticeRestController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UnitResidentService unitResidentService;

    @GetMapping
    public Map<String, Object> getNoticeList(
        @RequestParam("bldgIdParam") String bldgIdParam,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(required = false) String noticeType,
        @RequestParam(required = false) String searchType,
        @RequestParam(required = false) String searchWord,
        @RequestParam(required = false) String searchStartDate,
        @RequestParam(required = false) String searchEndDate,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
    	
    	
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());

        SimpleSearch simpleSearch = new SimpleSearch();
        simpleSearch.setBrdCode("N0001");
        simpleSearch.setBldgId(bldgIdParam);
        simpleSearch.setNoticeType(noticeType);
        simpleSearch.setSearchType(searchType);
        simpleSearch.setSearchWord(searchWord);
        simpleSearch.setSearchStartDate(searchStartDate);
        simpleSearch.setSearchEndDate(searchEndDate);

        PaginationInfo<NoticeVO> paging = new PaginationInfo<>();
        paging.setCurrentPageNo(page);
        paging.setSimpleSearch(simpleSearch);

        int total = noticeService.getTotalNoticeCount(paging);
        paging.setTotalRecordCount(total);

        List<NoticeVO> boardList = noticeService.getNoticeList(paging);

        List<String> residentBldgIds = units.stream()
            .map(UnitResidentVO::getBldgId).distinct().toList();

        final String targetBldgId = bldgIdParam;
        List<NoticeVO> filteredList = boardList.stream()
            .filter(notice -> {
                if (notice.getBldgId() == null) {
                    List<String> ownerBldgIds = unitResidentService.getUnitsByMember(notice.getMbrCd())
                        .stream().map(UnitResidentVO::getBldgId).distinct().toList();
                    return ownerBldgIds.contains(targetBldgId);
                } else {
                    return residentBldgIds.contains(notice.getBldgId());
                }
            }).toList();

        Map<String, Object> pagination = Map.of(
            "currentPageNo", paging.getCurrentPageNo(),
            "firstPageNoOnPageList", paging.getFirstPageNoOnPageList(),
            "lastPageNoOnPageList", paging.getLastPageNoOnPageList(),
            "totalPageCount", paging.getTotalPageCount()
        );

        return Map.of(
            "noticeList", filteredList,
            "pagination", pagination
        );
    }
}