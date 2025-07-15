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
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
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
	
	 // 📌 게시글 객체 준비
    @ModelAttribute("board")
    public ResidentBoardVO prepareBoard(
            @RequestParam(value = "rsdBrdId", required = false) String rsdBrdId,
            @RequestParam(value = "bldgIdParam", required = false) String bldgIdParam,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

        log.info("[prepareBoard] rsdBrdId={}, bldgIdParam={}", rsdBrdId, bldgIdParam);

        if(rsdBrdId != null && !rsdBrdId.isBlank()) {
            // rbId + bldgId 까지 채워주는 param VO
            ResidentBoardVO param = new ResidentBoardVO();
            param.setRsdBrdId(rsdBrdId);
            param.setBldgId(bldgIdParam);
            ResidentBoardVO vo = boardService.getBoard(param);
            log.info("[prepareBoard] 수정용 게시글 조회 결과: {}", vo);
            return vo != null ? vo : new ResidentBoardVO();
          }

        ResidentBoardVO newBoard = new ResidentBoardVO();
        if (bldgIdParam != null && !bldgIdParam.isBlank()) {
            newBoard.setBldgId(bldgIdParam);
        }
        return newBoard;
    }
	    /**
	     * 등록/수정 폼 진입.
	     * @param rsdBrdId 수정할 게시글 ID (optional)
	     * @param principal 로그인 사용자 정보
	     * @param model mode 전달용
	     */
    @GetMapping("/board/form")
    public String showCreateForm(
            @RequestParam(value = "rsdBrdId", required = false) String rsdBrdId,
            @RequestParam(value = "bldgIdParam", required = false) String bldgIdParam,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            Model model) {

        String mode = (rsdBrdId != null && !rsdBrdId.isEmpty()) ? "edit" : "new";
        model.addAttribute("mode", mode);

        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
        model.addAttribute("unitList", units);

        ResidentBoardVO board = (ResidentBoardVO) model.asMap().get("board");

        if ("edit".equals(mode)) {
            ResidentBoardVO original = boardService.getBoardById(rsdBrdId);
            if (original == null || !member.getMbrCd().equals(original.getMbrCd())) {
                return """
                    <script>
                      alert('작성자 본인만 수정할 수 있습니다.');
                      history.back();
                    </script>
                """;
            }
        }

        if (board != null && board.getBldgId() != null) {
            for (UnitResidentVO unit : units) {
                if (unit.getBldgId().equals(board.getBldgId())) {
                    model.addAttribute("buildingName", unit.getBuilding().getBldgNm());
                    break;
                }
            }
        }

        model.addAttribute("selectedBldgId", bldgIdParam);
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
	    						@RequestParam(value = "bldgIdParam",required = false) String bldgIdParam,
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
	        if (board.getBldgId() == null || board.getBldgId().isBlank()) {
	            model.addAttribute("mode", "new");
	            model.addAttribute("unitList", units);
	            model.addAttribute("error", "건물을 선택해주세요.");
	            return "resident/Board/BoardForm";
	        }
	        board.setMbrCd(member.getMbrCd());
	        board.setBrdCode("R0001"); // 자유게시판 고정
	        
	        if (board.getRsdBrdId() == null || board.getRsdBrdId().isEmpty()) {
	            boardService.insertBoard(board);
	        } else {
	            boardService.updateBoard(board);
	        }
	        return "redirect:/resident/board/detail?rsdBrdId=" + board.getRsdBrdId() + "&bldgIdParam=" + bldgIdParam;
	    }
	    
	    
	}
