package kr.or.ddit.admin.board.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        @RequestParam(name = "brdCode", required = false) String brdCode, // 여전히 허용
        Model model
    ) {
        // ① tab → brdCode로 변환
        if (brdCode == null || brdCode.isBlank()) {
            brdCode = switch (tab) {
                case "notice" -> "N"; // 공지사항
                case "faq" -> "F";    // FAQ
                case "qna" -> "Q";    // QNA
                default -> "N";
            };
        }

        // ② 페이징 설정
        PaginationInfo<BoardVO> paging = new PaginationInfo<>();
        paging.setCurrentPageNo(page);

        BoardVO condition = new BoardVO();
        condition.setBrdCode(brdCode); // 게시판 종류 조건
        paging.setDetailSearch(condition);

        // ③ 리스트 + 전체 수 조회
        int totalRecord = service.getTotalBoardRecord(paging);
        paging.setTotalRecordCount(totalRecord);

        List<BoardVO> boardList = service.readBoardList(paging);
        
        List<Map<String, Object>> processedList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (BoardVO board : boardList) {
            Map<String, Object> map = new HashMap<>();
            map.put("brdNo", board.getBrdNo());
            map.put("brdTitlNm", board.getBrdTitlNm());
            map.put("brdPblsDtmFormatted", board.getBrdPblsDtm() != null
                ? board.getBrdPblsDtm().format(formatter) : "-");
            map.put("brdVwCnt", board.getBrdVwCnt());
            map.put("brdCont", board.getBrdCont());
            map.put("boardCartegory", board.getBoardCartegory());
            map.put("notice", board.getNotice());
            // 필요한 다른 항목도 추가
            processedList.add(map);
        }

        // ④ 모델에 담기
        model.addAttribute("boardList", processedList);
        model.addAttribute("paging", paging);
        model.addAttribute("brdCode", brdCode);
        model.addAttribute("activeTab", tab); // ✅ 탭 강조용

        return "admin/notice/adminNoticeList"; 
    }

}
