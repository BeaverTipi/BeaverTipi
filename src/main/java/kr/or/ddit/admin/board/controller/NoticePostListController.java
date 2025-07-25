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
        @RequestParam(name = "tab", defaultValue = "notice") String tab,
        Model model
    ) {
        // ① 탭에 따라 BRD_CODE 결정
        String brdCode = switch (tab) {
            case "notice" -> "007";
            case "faq" -> "009";
            case "qna" -> "008";
            default -> "007";
        };

        // ② 페이징 및 검색 조건 구성
        PaginationInfo<BoardVO> paging = new PaginationInfo<>();
        paging.setCurrentPageNo(page);

        BoardVO condition = new BoardVO();
        condition.setBrdCode(brdCode);
        paging.setDetailSearch(condition);

        // ③ 전체 레코드 수 조회
        int totalRecord = service.getTotalBoardRecord(paging);
        paging.setTotalRecordCount(totalRecord);

        // ④ 게시글 리스트 조회 (BOARD + FAQ/QNA 조인 포함)
        List<BoardVO> boardList = service.readBoardList(paging);

        // ⑤ View로 전달
        model.addAttribute("boardList", boardList);
        model.addAttribute("paging", paging);
        model.addAttribute("codeValue", brdCode); // 탭 식별용
        model.addAttribute("activeTab", tab);     // 탭 활성화용

        return "admin/notice/adminNoticeList";
    }

}
