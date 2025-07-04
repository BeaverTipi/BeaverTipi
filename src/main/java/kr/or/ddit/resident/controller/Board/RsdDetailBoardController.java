package kr.or.ddit.resident.controller.Board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.service.board.ResidentBoardService;
import kr.or.ddit.resident.service.unitResident.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;
import kr.or.ddit.vo.UnitResidentVO;

@Controller
@RequestMapping("/resident")
public class RsdDetailBoardController {

	@Autowired
	private ResidentBoardService service;
	
	@Autowired
	private UnitResidentService unitResidentService;
	
	@GetMapping("/board/detail")
	public String showBoardDetail(
				@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
				@RequestParam("rsdBrdId") String rsdBrdId
				,Model model
			) {
			MemberVO member = principal.getRealUser();
		
			List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
			if(units == null || units.isEmpty()) {
				return "redirect:/";
			}
			
			String bldgId = units.get(0).getBldgId();

			 ResidentBoardVO param = new ResidentBoardVO();
			    param.setRsdBrdId(rsdBrdId);
			    param.setBldgId(bldgId);


			
			service.viewCount(param);
			ResidentBoardVO board = service.getBoard(param);
			
			model.addAttribute("board", board);
		
		return "resident/Board/BoardDetail";
	}
	
}
