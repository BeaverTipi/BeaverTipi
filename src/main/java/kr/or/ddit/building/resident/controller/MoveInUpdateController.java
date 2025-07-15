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
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
public class MoveInUpdateController {

    @Autowired
    private MoveInService moveInService;

    @PostMapping("/update")
    public ResponseEntity<String> updateResident(@RequestBody UnitResidentVO vo) {
        log.info("입주민 수정 요청: {}", vo);
        int result = moveInService.updateResident(vo);
        return ResponseEntity.ok(result > 0 ? "SUCCESS" : "FAIL");
    }
}