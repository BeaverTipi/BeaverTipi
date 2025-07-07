package kr.or.ddit.resident.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.board.service.ResidentBoardService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
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
	public String showBoardDetail(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
	                              @RequestParam("rsdBrdId") String rsdBrdId,
	                              Model model) {

	    MemberVO member = principal.getRealUser();
	    List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());

	    if (units == null || units.isEmpty()) {
	        return "redirect:/"; // 건물 정보 없음
	    }

	    String bldgId = units.get(0).getBldgId();

	    ResidentBoardVO param = new ResidentBoardVO();
	    param.setRsdBrdId(rsdBrdId);
	    param.setBldgId(bldgId);

	    // 조회수 증가
	    service.viewCount(param);

	    // 게시글 조회
	    ResidentBoardVO board = service.getBoard(param);

	    if (board == null) {
	        // 게시글이 없거나 다른 건물의 글일 경우
	        return "redirect:/resident/board";
	    }

	    model.addAttribute("board", board);
	    return "resident/Board/BoardDetail";
	}
	
}
