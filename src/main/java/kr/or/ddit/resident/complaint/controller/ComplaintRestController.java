package kr.or.ddit.resident.complaint.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.resident.complaint.service.ComplaintService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.renderer.PaginationRenderer;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ajax/resident/api/complaints")
@RequiredArgsConstructor
public class ComplaintRestController {

	@Autowired
    private ComplaintService complaintService;
	@Autowired
    private UnitResidentService unitResidentService;
	@Autowired
    private CommonCodeService codeService;
	@Autowired
    private PaginationRenderer paginationRenderer;

    @GetMapping
    public Map<String, Object> getComplaintList(
        @RequestParam("bldgIdParam") String bldgIdParam,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "searchType", required = false) String searchType,
        @RequestParam(value = "searchWord", required = false) String searchWord,
        @RequestParam(value = "openYn", required = false) String openYn,
        @RequestParam(value = "reqStatus", required = false) String reqStatus,
        @RequestParam(value = "searchStartDate", required = false) String searchStartDate,
        @RequestParam(value = "searchEndDate", required = false) String searchEndDate,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        MemberVO member = principal.getRealUser();
        SimpleSearch search = new SimpleSearch();
        search.setLoginMbrCd(member.getMbrCd());
        search.setBldgId(bldgIdParam);
        search.setSearchType(searchType);
        search.setSearchWord(searchWord);
        search.setOpenYn(openYn);
        search.setReqStatus(reqStatus);
        search.setSearchStartDate(searchStartDate);
        search.setSearchEndDate(searchEndDate);
        search.setBrdCode("M0001");

        PaginationInfo<ResidentBoardVO> pagingInfo = new PaginationInfo<>();
        pagingInfo.setCurrentPageNo(page);
        pagingInfo.setPageSize(10);
        pagingInfo.setSimpleSearch(search);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("search", search);
        int totalCount = complaintService.selectComplaintCount(paramMap);
        pagingInfo.setTotalRecordCount(totalCount);
        paramMap.put("paging", pagingInfo);

        List<ResidentBoardVO> postList = complaintService.selectComplaintList(paramMap);
        String pagingHtml = paginationRenderer.renderPagination(pagingInfo, "loadComplaints");

        Map<String, Object> result = new HashMap<>();
        result.put("postList", postList);
        result.put("pagination", pagingHtml);
        result.put("loginMbrCd", member.getMbrCd());
        return result;
    }
}
