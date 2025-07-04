package kr.or.ddit.resident.controller.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.or.ddit.resident.service.board.ResidentBoardService;
import kr.or.ddit.resident.service.unitResident.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;
import kr.or.ddit.vo.UnitResidentVO;

@Controller
@RequestMapping("/resident")
public class RsdCreateBoardController {

	@Autowired
	private ResidentBoardService boardService;
	
	@Autowired
	private UnitResidentService unitResidentService;
	
	@GetMapping("/board/form")
	public String showCreateForm(Model model) {
		model.addAttribute("board",new ResidentBoardVO());
		return "resident/Board/BoardForm";
	}
	
	@PostMapping("/board")
	public String createBoard(	
			ResidentBoardVO board,
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal
			)
	{
		MemberVO member = principal.getRealUser();
		List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
		if(units==null || units.isEmpty()) {
			return "redirect:/resident/board/form";
		}
		board.setBldgId(units.get(0).getBldgId());
		
		board.setMbrCd(member.getMbrCd());
		
		boardService.insertBoard(board);
		return "redirect:/resident/board";
	}
	
}
