package kr.or.ddit.resident.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.resident.board.service.ResidentBoardService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/ajax/resident/api/board")
public class RsdBoardRestController {

    @Autowired
    private ResidentBoardService boardService;

    @GetMapping
    public Map<String, Object> getBoardsByBuilding(
            @RequestParam("bldgIdParam") String bldgIdParam,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchWord", required = false) String searchWord,
            @RequestParam(value = "searchStartDate", required = false) String searchStartDate,
            @RequestParam(value = "searchEndDate", required = false) String searchEndDate,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        SimpleSearch search = new SimpleSearch();
        search.setBldgId(bldgIdParam);
        search.setSearchType(searchType);
        search.setSearchWord(searchWord);
        search.setSearchStartDate(searchStartDate);
        search.setSearchEndDate(searchEndDate);

        PaginationInfo<ResidentBoardVO> paging = new PaginationInfo<>();
        paging.setSimpleSearch(search);
        paging.setCurrentPageNo(page);
        paging.setRecordCountPerPage(10);
        paging.setPageSize(5);

        List<ResidentBoardVO> boardList = boardService.getBoardList(paging);

        Map<String, Object> result = new HashMap<>();
        result.put("postList", boardList);
        result.put("pagination", paging); // JS에서 totalPageCount, currentPageNo 등을 활용 가능

        return result;
    }
}