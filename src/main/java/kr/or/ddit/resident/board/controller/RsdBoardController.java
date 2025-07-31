package kr.or.ddit.resident.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.board.service.ResidentBoardService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident")
public class RsdBoardController {

    @Autowired
    private ResidentBoardService boardService;

    @Autowired
    private UnitResidentService unitResidentService;
    
    @GetMapping("/board")
    public String readResidentBoard(
            Model model,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(value = "search.bldgId", required = false) String search_bldgId,  // search.bldgId로 받기
            @RequestParam(value = "myPostsOnly", required = false)String myPostsOnly,
            @ModelAttribute("search") SimpleSearch simpleSearch,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

        log.info("🔎 noticeType = {}", simpleSearch.getNoticeType());
        log.info("📅 searchStartDate = {}", simpleSearch.getSearchStartDate());
        log.info("📅 searchEndDate = {}", simpleSearch.getSearchEndDate());
        log.info("📌 search_bldgId: {}", search_bldgId);
        
        // 입주민의 유닛 정보 확인
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
        if (units == null || units.isEmpty()) {
            return "redirect:/member/register";
        }

        log.info("📌 search_bldgId: {}", search_bldgId);
        
        if (search_bldgId == null || search_bldgId.isEmpty()) {
            search_bldgId = units.get(0).getBldgId();
        }

        // 검색 파라미터로 받은 bldgId를 SimpleSearch 객체에 세팅
        simpleSearch.setBldgId(search_bldgId);  // search.bldgId를 simpleSearch에 설정
        simpleSearch.setBrdCode("R0001");
        simpleSearch.setLoginMbrCd(member.getMbrCd());
        simpleSearch.setMyPostsOnly("Y".equalsIgnoreCase(myPostsOnly));

        // 나머지 로직은 그대로
        PaginationInfo<ResidentBoardVO> paging = new PaginationInfo<>();
        paging.setCurrentPageNo(page);
        paging.setSimpleSearch(simpleSearch);

        int totalRecord = boardService.getTotalRecord(paging);
        paging.setTotalRecordCount(totalRecord);
        List<ResidentBoardVO> boardList = boardService.getBoardList(paging);
        String pagingHTML = new DefaultPaginationRenderer().renderPagination(paging, "fnPaging");
        
        log.info("🧩 brdCode = {}", simpleSearch.getBrdCode());
        // 모델에 데이터 바인딩
        model.addAttribute("search", simpleSearch);
        model.addAttribute("unitList", units);
        model.addAttribute("selectedBldgId", search_bldgId);
        model.addAttribute("boardList", boardList);
        model.addAttribute("pagingHTML", pagingHTML);
        model.addAttribute("pagingInfo", paging);

        return "resident/Board/ResidentBoard";
    }

//    
//    @GetMapping("/board/trash")
//    public String readTrash(
//            Model model,
//            @RequestParam(value="page", defaultValue="1") int page,
//            @RequestParam(value="bldgIdParam", required=false) String bldgIdParam,
//            @ModelAttribute("search") SimpleSearch simpleSearch,
//            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
//
//        // 1) 입주민 정보 & 유닛 조회
//        MemberVO member = principal.getRealUser();
//        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
//        if(units == null || units.isEmpty()) {
//            return "redirect:/member/register";
//        }
//
//        // 2) 선택된 건물 결정 (파라미터 우선, 없으면 첫 유닛)
//        String selectedBldgId = (bldgIdParam != null && !bldgIdParam.isBlank())
//                                ? bldgIdParam
//                                : units.get(0).getBldgId();
//
//        // 3) 검색 조건에 건물 ID 설정
//        simpleSearch.setBldgId(selectedBldgId);
//
//        // 4) 페이징 정보 세팅
//        PaginationInfo<ResidentBoardVO> paging = new PaginationInfo<>();
//        paging.setCurrentPageNo(page);
//        paging.setSimpleSearch(simpleSearch);
//
//        // 5) 삭제된 게시글 전체 건수 & 목록 조회
//        int deletedTotal = boardService.getDeletedTotalCount(paging);
//        paging.setTotalRecordCount(deletedTotal);
//        List<ResidentBoardVO> deletedList 
//        = boardService.getDeletedBoardList(paging);
//
//
//        // 6) 페이징 HTML 생성
//        String pagingHTML = new DefaultPaginationRenderer()
//                              .renderPagination(paging, "fnPaging");
//
//        // 7) 모델 바인딩
//        model.addAttribute("unitList", units);
//        model.addAttribute("selectedBldgId", selectedBldgId);
//        model.addAttribute("boardList", deletedList);
//        model.addAttribute("pagingHTML", pagingHTML);
//        model.addAttribute("pagingInfo", paging);
//
//        return "resident/Board/BoardTrash";
//    }
//
//    /** 휴지통에서 복구(관리자 전용) */
//    @PostMapping("/board/restore")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String restoreBoard(
//            @RequestParam("rsdBrdId") String rsdBrdId,
//            @RequestParam("bldgIdParam") String bldgIdParam) {
//
//        boardService.restoreBoard(rsdBrdId);
//        return "redirect:/resident/board/trash?bldgIdParam=" + bldgIdParam;
//    }
//
//    /** 휴지통에서 영구 삭제(관리자 전용) */
//    @PostMapping("/board/permanent")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String permanentDelete(
//            @RequestParam("rsdBrdId") String rsdBrdId,
//            @RequestParam("bldgIdParam") String bldgIdParam) {
//
//        boardService.permanentDeleteBoard(rsdBrdId);
//        return "redirect:/resident/board/trash?bldgIdParam=" + bldgIdParam;
//    }
    
}
