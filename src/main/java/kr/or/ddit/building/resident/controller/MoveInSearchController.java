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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import kr.or.ddit.building.resident.service.MoveInService;
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
@Slf4j
@Controller
@RequestMapping("/building/move-in")
public class MoveInSearchController {

    

    @Autowired
    private MoveInService moveInService;

    @GetMapping("/searchMember")
    public ResponseEntity<Map<String, Object>> searchMember(@RequestParam String keyword) {
        log.info("모달 유저 검색 요청 - 키워드: {}", keyword);
        List<MemberVO> result = moveInService.searchMember(keyword);

        Map<String, Object> response = new HashMap<String, Object>(); // ✅ 1.6 호환
        response.put("status", "OK");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }
}