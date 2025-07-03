package kr.or.ddit.resident.controller.Board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.service.ResidentBoardService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.util.renderer.PaginationRenderer;
import kr.or.ddit.vo.ResidentBoardVO;

@Controller
@RequestMapping("/resident")
public class RsdBoardController {
	
	@Autowired
	private ResidentBoardService boardService;
	
		@GetMapping("/board")
		public String readesidentBoard(
				Model model,
				@RequestParam(required = false, defaultValue = "1")int page,
				@ModelAttribute("search") SimpleSearch simpleSearch
				) {
				PaginationInfo<ResidentBoardVO> paging = new PaginationInfo();
				paging.setCurrentPageNo(page);
				paging.setSimpleSearch(simpleSearch);
				
				int totalRecord = boardService.getTotalRecord(paging);
				paging.setTotalRecordCount(totalRecord);
				List<ResidentBoardVO> boardList = boardService.getBoardList(paging);
				
				//PaginationRenderer renderer = new DefaultPaginationRenderer();
				String pagingHTML = new DefaultPaginationRenderer().renderPagination(paging, "fnPaging");
				
				model.addAttribute("boardList",boardList);
				model.addAttribute("pagingHTML",pagingHTML);
				
			 return "resident/Board/ResidentBoard"; 
		}
		
		
}
