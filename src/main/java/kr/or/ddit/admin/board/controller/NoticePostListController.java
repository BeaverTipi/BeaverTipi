package kr.or.ddit.admin.board.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        Model model
    ) {
        // ① 공통 코드 CODE_VALUE 결정 (brdCode 대신)
        String codeValue = switch (tab) {
            case "notice" -> "007";
            case "faq" -> "009";
            case "qna" -> "008";
            default -> "007";
        };

        // ② 페이징 설정
        PaginationInfo<BoardVO> paging = new PaginationInfo<>();
        paging.setCurrentPageNo(page);

        BoardVO condition = new BoardVO();
        condition.setBrdCtgryGrpCd("BRDCT"); // 그룹코드는 유지
        paging.setDetailSearch(condition);

        // ③ 전체 레코드 수 조회
        int totalRecord = service.getTotalBoardRecord(paging);
        paging.setTotalRecordCount(totalRecord);

        // ④ 게시글 목록 조회
        List<Map<String, Object>> boardList = service.readBoardList(paging);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> processedList = new ArrayList<>();

        for (Map<String, Object> board : boardList) {
            Map<String, Object> map = new HashMap<>();
            map.put("brdTitlNm", board.get("BRD_TITL_NM"));
            map.put("brdCont", board.get("BRD_CONT"));
            map.put("brdVwCnt", board.get("BRD_VW_CNT"));
            map.put("brdNo", board.get("BRD_NO"));

            // ✅ 공통 코드 이름 (CODE_NAME)
            map.put("brdCtgryName", board.get("BRD_CTGRY_NAME")); // CODE_NAME 기준

            Date brdPblsDtm = (Date) board.get("BRD_PBLS_DTM");
            Date brdEndDtm = (Date) board.get("BRD_END_DTM");
            map.put("brdPblsDtmFormatted", brdPblsDtm != null ? formatter.format(brdPblsDtm) : "-");
            map.put("brdEndDtmFormatted", brdEndDtm != null ? formatter.format(brdEndDtm) : "-");

            processedList.add(map);
        }

        // ⑤ 모델에 담기
        model.addAttribute("boardList", processedList);
        model.addAttribute("paging", paging);
        model.addAttribute("codeValue", codeValue); // ✅ 탭 기준으로 codeValue 전달
        model.addAttribute("activeTab", tab); // 탭 활성화

        return "admin/notice/adminNoticeList";
    }

}
