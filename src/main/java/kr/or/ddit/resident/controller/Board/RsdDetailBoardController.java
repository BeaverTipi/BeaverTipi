package kr.or.ddit.resident.controller.Board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.service.board.ResidentBoardService;
import kr.or.ddit.vo.ResidentBoardVO;

@Controller
@RequestMapping("/resident")
public class RsdDetailBoardController {

	@Autowired
	private ResidentBoardService service;
	
	@GetMapping("/board/detail")
	public String showBoardDetail(
				@RequestParam("rsdBrdId") String rsdBrdId
				,Model model
			) {
			service.viewCount(rsdBrdId);
			
			ResidentBoardVO board = service.getBoard(rsdBrdId);
			model.addAttribute("board", board);
		
		return "resident/Board/BoardDetail";
	}
	
}
