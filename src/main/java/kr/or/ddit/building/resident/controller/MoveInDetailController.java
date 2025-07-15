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


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;


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

    @GetMapping("/detail")
    public String moveInDetailView() {
        log.info("입주관리 상세 페이지 진입");
        return "building/move-in/moveInDetail";
        
    }
}