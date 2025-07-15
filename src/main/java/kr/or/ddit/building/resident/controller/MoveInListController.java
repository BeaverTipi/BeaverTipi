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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
public class MoveInListController {

    @Autowired
    private MoveInService moveInService;

    @GetMapping("/list/{bldgId}")
    public ResponseEntity<List<UnitResidentVO>> getResidentsByBldgId(@PathVariable String bldgId) {
        log.info("입주민 목록 조회 요청 - 건물 ID: {}", bldgId);
        List<UnitResidentVO> list = moveInService.getResidentsByBldgId(bldgId);
        return ResponseEntity.ok(list);
    }
}