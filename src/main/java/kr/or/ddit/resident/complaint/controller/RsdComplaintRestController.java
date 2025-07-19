package kr.or.ddit.resident.complaint.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.resident.complaint.service.ComplaintService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;

@RestController
@RequestMapping("/ajax/resident/api/complaints")
public class RsdComplaintRestController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping
    public List<Map<String, Object>> getComplaintsByBuilding(
            @RequestParam("bldgIdParam") String bldgIdParam,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

        MemberVO loginUser = principal.getRealUser();

        SimpleSearch search = new SimpleSearch();
        search.setBldgId(bldgIdParam);
        search.setBrdCode("M0001");

        PaginationInfo<ResidentBoardVO> paging = new PaginationInfo<>();
        paging.setSimpleSearch(search);
        paging.setCurrentPageNo(page);
        paging.setRecordCountPerPage(10);
        paging.setPageSize(5);

        int totalCount = complaintService.selectComplaintCount(Map.of("search", search));
        paging.setTotalRecordCount(totalCount);

        Map<String, Object> param = Map.of("search", search, "paging", paging);
        List<ResidentBoardVO> list = complaintService.selectComplaintList(param);

        // 👇 게시글 데이터를 Map으로 변환하면서 작성자인지 판단
        return list.stream().map(post -> {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("rsdBrdId", post.getRsdBrdId());
            map.put("rsdBrdTitl", post.getRsdBrdTitl());
            map.put("rsdBrdPblsDtm", post.getRsdBrdPblsDtm());
            map.put("mbrNnm", post.getMbrNnm());
            map.put("mbrCd", post.getMbrCd());
            map.put("bldgId", post.getBldgId());
            map.put("openYn", post.getOpenYn());
            map.put("reqStatus", post.getReqStatus());
            map.put("isAuthor", post.getMbrCd().equals(loginUser.getMbrCd())); // ✨ 핵심!
            return map;
        }).toList();
    }

}



