package kr.or.ddit.resident.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.resident.board.service.ResidentBoardService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.vo.ResidentBoardVO;

@RestController
@RequestMapping("/ajax/resident/api/board")
public class RsdBoardRestController {

    @Autowired
    private ResidentBoardService boardService;

    @GetMapping
    public List<ResidentBoardVO> getBoardsByBuilding(@RequestParam("bldgIdParam") String bldgIdParam,
                                                      @RequestParam(value = "page", defaultValue = "1") int page) {
        // SimpleSearch 객체 생성 및 bldgIdParam 설정
        SimpleSearch search = new SimpleSearch();
        search.setBldgId(bldgIdParam);  // URL 파라미터로 받은 bldgIdParam을 설정

        // PaginationInfo 객체 생성 및 검색 정보 설정
        PaginationInfo<ResidentBoardVO> paging = new PaginationInfo<>();
        paging.setSimpleSearch(search);  // simpleSearch 객체를 세팅
        paging.setCurrentPageNo(page);  // 요청받은 페이지 설정

        // 게시글 목록을 반환
        return boardService.getBoardList(paging);  // 게시글 목록 반환 (JSON으로 자동 변환)
    }
}
