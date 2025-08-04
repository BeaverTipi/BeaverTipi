/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 15.     			윤현식            최초 생성
 *
 * </pre>
 */
package kr.or.ddit.building.resident.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.building.resident.dto.residentListDTO;
import kr.or.ddit.building.resident.service.MoveInService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 
 * @since
 * @see
 * 만든이 이학범 ㅋ
 *
 */
/**
 * <pre>
 * 입주관리 상세 페이지 진입 컨트롤러
 * </pre>
 * 
 * @author 이학범
 * @since 2025.07.15
 */
@Slf4j
@Controller
@RequestMapping("/building/move-in")
public class MoveInDetailController {
	
	@Autowired
	private MoveInService moveInService;

    @GetMapping("/detail")
    public String moveInDetailView(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,Model model) {
        MemberVO member = principal.getRealUser();
        String rentalPtyId = member.getTenancy().getRentalPtyId();
    	log.info("입주관리 상세 페이지 진입");
        List<residentListDTO> buildingList = moveInService.readBuildingsUnitAll(rentalPtyId);
        model.addAttribute("buildingList", buildingList);
        return "building/move-in/moveInDetail";
        
    }
    @GetMapping("/buildingList")
    @ResponseBody
    public ResponseEntity<List<BuildingVO>> getBuildingListByRentalPty(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        MemberVO member = principal.getRealUser();
        String rentalPtyId = member.getTenancy().getRentalPtyId();
        List<BuildingVO> buildingList = moveInService.getBuildingsByRentalPtyId(rentalPtyId);
        return ResponseEntity.ok(buildingList);
    }
}
