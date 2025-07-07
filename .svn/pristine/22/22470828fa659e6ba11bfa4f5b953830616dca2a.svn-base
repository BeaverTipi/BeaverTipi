package kr.or.ddit.resident.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	        @RequestParam(required = false) String bldgIdParam,
	        @ModelAttribute("search") SimpleSearch simpleSearch,
	        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

	    MemberVO member = principal.getRealUser();
	    List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());

	    if (units == null || units.isEmpty()) {
	        return "redirect:/member/register";
	    }

	    // 🔄 건물 선택 처리
	    String selectedBldgId = (bldgIdParam != null) ? bldgIdParam : units.get(0).getBldgId();
	    simpleSearch.setBldgId(selectedBldgId);

	    // 페이징 처리
	    PaginationInfo<ResidentBoardVO> paging = new PaginationInfo<>();
	    paging.setCurrentPageNo(page);
	    paging.setSimpleSearch(simpleSearch);

	    int totalRecord = boardService.getTotalRecord(paging);
	    paging.setTotalRecordCount(totalRecord);
	    List<ResidentBoardVO> boardList = boardService.getBoardList(paging);

	    String pagingHTML = new DefaultPaginationRenderer().renderPagination(paging, "fnPaging");

	    model.addAttribute("unitList", units);
	    model.addAttribute("selectedBldgId", selectedBldgId);
	    model.addAttribute("boardList", boardList);
	    model.addAttribute("pagingHTML", pagingHTML);
	    model.addAttribute("pagingInfo", paging);

	    return "resident/Board/ResidentBoard";
	}


}