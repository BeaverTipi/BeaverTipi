package kr.or.ddit.resident.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.board.service.ResidentBoardService;
import kr.or.ddit.resident.boardCartegory.service.BoardCartegoryService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BoardCartegoryVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident")
public class RsdCreateBoardController {

	@Autowired
	private ResidentBoardService boardService;
	
	@Autowired
	private UnitResidentService unitResidentService;
	
	 @ModelAttribute("board")
	    public ResidentBoardVO prepareBoard(
	            @RequestParam(value="rsdBrdId", required=false) String rsdBrdId,
	            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
	        log.info("[prepareBoard] rsdBrdId={}", rsdBrdId);
	        if (rsdBrdId != null && !rsdBrdId.isEmpty()) {
	            MemberVO member = principal.getRealUser();
	            List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
	            if (units != null && !units.isEmpty()) {
	                String bldgId = units.get(0).getBldgId();
	                ResidentBoardVO param = new ResidentBoardVO();
	                param.setRsdBrdId(rsdBrdId);
	                param.setBldgId(bldgId);
	                ResidentBoardVO vo = boardService.getBoard(param);
	                log.info("[prepareBoard] service.getBoard() → vo={}", vo);
	                if (vo != null) return vo;
	            }
	        }
	        // 새 글 폼용 빈 VO
	        return new ResidentBoardVO();
	    }



	    /**
	     * 등록/수정 폼 진입.
	     * @param rsdBrdId 수정할 게시글 ID (optional)
	     * @param principal 로그인 사용자 정보
	     * @param model mode 전달용
	     */
	    @GetMapping("/board/form")
	    public String showCreateForm(@RequestParam(value = "rsdBrdId", required = false) String rsdBrdId,
	                                 @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
	                                 Model model) {
	        String mode = (rsdBrdId != null && !rsdBrdId.isEmpty()) ? "edit" : "new";
	        model.addAttribute("mode", mode);
	        return "resident/Board/BoardForm";
	    }

	    /**
	     * 등록/수정 저장 처리.
	     * @param board prepareBoard로 등록된 VO
	     * @param principal 로그인 사용자
	     * @param model 실패 시 mode 재설정용
	     */
	    @PostMapping("/board")
	    public String saveBoard(@ModelAttribute("board") ResidentBoardVO board,
	                            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
	                            Model model) {

	        MemberVO member = principal.getRealUser();
	        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());

	        if (units == null || units.isEmpty()) {
	            String mode = (board.getRsdBrdId() == null || board.getRsdBrdId().isEmpty())
	                          ? "new" : "edit";
	            model.addAttribute("mode", mode);
	            return "resident/Board/BoardForm";
	        }

	        board.setBldgId(units.get(0).getBldgId());
	        board.setMbrCd(member.getMbrCd());
	        board.setBrdCode("R0001"); // 자유게시판 고정

	        if (board.getRsdBrdId() == null || board.getRsdBrdId().isEmpty()) {
	            boardService.insertBoard(board);
	        } else {
	            boardService.updateBoard(board);
	        }

	        return "redirect:/resident/board";
	    }
	}
