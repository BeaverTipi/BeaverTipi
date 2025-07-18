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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import kr.or.ddit.building.resident.service.MoveInService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 
 * @since
 * @see
 * 만든이 이학범 ㅋ
 *
 */
@Slf4j
@Controller
@RequestMapping("/building/move-in")
public class MoveInSearchController {

    

    @Autowired
    private MoveInService moveInService;
    
    // 1. 건물 전체 리스트 반환
    @GetMapping("/searchbuildingList")
    public ResponseEntity<List<BuildingVO>> getBuildingList(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        String rentalPtyId = principal.getRealUser().getTenancy().getRentalPtyId();
        List<BuildingVO> buildingList = moveInService.getBuildingsByRentalPtyId(rentalPtyId);
        return ResponseEntity.ok(buildingList);
    }

    // 2. 해당 건물의 '공실' 유닛만 반환
    @GetMapping("/vacantUnits/{bldgId}")
    public ResponseEntity<List<UnitVO>> getVacantUnits(@PathVariable("bldgId") String bldgId) {
        List<UnitVO> unitList = moveInService.getVacantUnitList(bldgId);
        return ResponseEntity.ok(unitList);
    }


    @GetMapping("/searchMember")
    public ResponseEntity<Map<String, Object>> searchMember(@RequestParam String keyword) {
        log.info("모달 유저 검색 요청 - 키워드: {}", keyword);
        List<MemberVO> result = moveInService.searchMember(keyword);

        Map<String, Object> response = new HashMap<String, Object>(); 
        response.put("status", "OK");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }
}