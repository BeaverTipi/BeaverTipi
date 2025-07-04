package kr.or.ddit.resident.controller.Board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.service.board.ResidentBoardService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident")
public class RsdBoardController {

	@Autowired
	private ResidentBoardService boardService;

	@GetMapping("/board")
	public String readesidentBoard(Model model, @RequestParam(required = false, defaultValue = "1") int page,
			@ModelAttribute("search") SimpleSearch simpleSearch,
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
			MemberVO member = principal.getRealUser();
			log.info("memberVO 확인=========================={}", member);
		
		  BuildingVO bldg = (BuildingVO)principal.
		  log.info("건물VO 확인=========================={}",bldg); if(bldg == null) {
		  return "redirect:/account/register"; }
		  
		  String bldgId = bldg.getBldgId(); simpleSearch.setBldgId(bldgId);
		  
		  PaginationInfo<ResidentBoardVO> paging = new PaginationInfo();
		  paging.setCurrentPageNo(page); paging.setSimpleSearch(simpleSearch);
		  
		  int totalRecord = boardService.getTotalRecord(paging);
		  paging.setTotalRecordCount(totalRecord); List<ResidentBoardVO> boardList =
		  boardService.getBoardList(paging);
		  
		  //PaginationRenderer renderer = new DefaultPaginationRenderer(); String
		  pagingHTML = new DefaultPaginationRenderer().renderPagination(paging,
		  "fnPaging");
		  
		  model.addAttribute("boardList",boardList);
		  model.addAttribute("pagingHTML",pagingHTML);
		  model.addAttribute("pagingInfo",paging);
		 

		return "resident/Board/ResidentBoard";
	}

}
