package kr.or.ddit.resident.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class RsdDetailBoardController {

    @Autowired
    private ResidentBoardService service;

    @Autowired
    private UnitResidentService unitResidentService;

    @GetMapping("/board/detail")
    public String showBoardDetail(
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
        @RequestParam("rsdBrdId") String rsdBrdId,
        @RequestParam(value="bldgIdParam", required=false) String bldgIdParam,
        @RequestParam(value = "page",required = false,defaultValue = "1") int page,
        Model model
    ) {
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
        if(units == null || units.isEmpty()) {
            return "redirect:/";
        }
        
        
        // 파라미터가 있으면 그것을, 없으면 첫 유닛의 bldgId
        String bldgId = (bldgIdParam != null && !bldgIdParam.isBlank())
                        ? bldgIdParam
                        : units.get(0).getBldgId();

        ResidentBoardVO param = new ResidentBoardVO();
        param.setRsdBrdId(rsdBrdId);
        param.setBldgId(bldgId);

        service.viewCount(param);
        ResidentBoardVO board = service.getBoard(param);
        if(board == null) {
            // 글이 없을 땐 동일 파라미터를 붙여서 목록으로
            return "redirect:/resident/board?bldgIdParam=" + bldgId;
        }

        // ▶ 이 줄을 꼭, bldgIdParam 이 아니라 bldgId 문자열로 넘겨야 합니다
        model.addAttribute("selectedBldgId", bldgId);
        model.addAttribute("board", board);
        model.addAttribute("loginUser", member);
        model.addAttribute("page", page);
        model.addAttribute("bldgIdParam", bldgIdParam);

        return "resident/Board/BoardDetail";
    }
    
    @PostMapping("/board/delete")
    @ResponseBody
    public String deleteBoard(
    			@RequestParam("rsdBrdId") String rsdBrdId,
    			@RequestParam("bldgIdParam") String bldgIdParam,
    			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
    			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
    			Model model
    		) {
    		MemberVO loginMember = principal.getRealUser();
    		ResidentBoardVO board = service.getBoardById(rsdBrdId);
    		
    		if(board == null || !loginMember.getMbrCd().equals(board.getMbrCd())) {
    			 return """
    		               <script>
    		                 alert('작성자 본인만 삭제할 수 있습니다.');
    		                 history.back();
    		               </script>
    		               """;
    		}
    		
    	
    	
    	
    	service.softDeleteBoard(rsdBrdId);
    	return """
    		    <script src='https://cdn.jsdelivr.net/npm/sweetalert2@11'></script>
    		    <script>
    		      Swal.fire({
    		        icon: 'success',
    		        title: '삭제되었습니다.',
    		        confirmButtonColor: '#E17100',
    		        confirmButtonText: '확인'
    		      }).then(() => {
    		        location.href = '/resident/board?bldgIdParam=%s&page=%d';
    		      });
    		    </script>
    		    """.formatted(bldgIdParam, page);
    }
}

