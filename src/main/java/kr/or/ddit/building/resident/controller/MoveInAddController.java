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




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import kr.or.ddit.building.resident.service.MoveInService;

import kr.or.ddit.vo.UnitResidentVO;
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
public class MoveInAddController {

    @Autowired
    private MoveInService moveInService;

    @PostMapping("/register")
    public ResponseEntity<String> registerResident(
        @RequestBody UnitResidentVO vo
        /* 필요하면 @AuthenticationPrincipal RealUserWrapper<MemberVO> principal */
    ) {
        // 걍임대인ID도 받아와버려 썅!
        log.info("=== 입주민 등록 시도 ===");
        log.info("unitId: {}", vo.getUnitId());
        log.info("bldgId: {}", vo.getBldgId());
        log.info("rentalPtyId: {}", vo.getRentalPtyId());
        log.info("mbrCd: {}", vo.getMbrCd());
        log.info("moveInDt: {}", vo.getMoveInDt());

        // (선택) rentalPtyId null 체크
        if (vo.getRentalPtyId() == null || vo.getRentalPtyId().isEmpty()) {
            log.error("rentalPtyId 값이 없습니다!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("임대사업자 정보 누락");
        }

        int result = moveInService.registerResident(vo);
        return ResponseEntity.ok(result > 0 ? "SUCCESS" : "FAIL");
    }
   
}