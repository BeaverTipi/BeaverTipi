package kr.or.ddit.admin.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.admin.board.service.NoticePostService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticePostListController {

    private final NoticePostService service;

    @GetMapping("/list")
    public String boardList(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "brdCode", required = false) String brdCode, // 카테고리 필터
        Model model
    ) {
        PaginationInfo<BoardVO> paging = new PaginationInfo<>();
        paging.setCurrentPageNo(page);

        BoardVO condition = new BoardVO();
        condition.setBrdCode(brdCode);
        paging.setDetailSearch(condition);

        // 페이징 및 리스트 조회
        int totalRecord = service.getTotalBoardRecord(paging);
        paging.setTotalRecordCount(totalRecord);

        List<BoardVO> boardList = service.readBoardList(paging);

        model.addAttribute("boardList", boardList);
        model.addAttribute("paging", paging);
        model.addAttribute("brdCode", brdCode); 

        return "admin/notice/adminNoticeList"; 
    }
}
