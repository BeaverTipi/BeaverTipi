package kr.or.ddit.resident.controller.Board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.or.ddit.resident.service.board.ResidentBoardService;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;

@Controller
@RequestMapping("/resident")
public class RsdCreateBoardController {

	@Autowired
	private ResidentBoardService boardService;
	
	@GetMapping("/board/form")
	public String showCreateForm(Model model) {
		model.addAttribute("board",new ResidentBoardVO());
		return "resident/Board/BoardForm";
	}
	
	@PostMapping("/board")
	public String createBoard(	
			@ModelAttribute("board") ResidentBoardVO board,
			HttpSession session
			)
	{
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		if(loginUser!=null) {
			board.setMbrCd(loginUser.getMbrCd());
		}else {
			board.setMbrCd("anonymous");
		}
		
		boardService.insertBoard(board);
		return "redirect:/resident/board";
	}
	
}
