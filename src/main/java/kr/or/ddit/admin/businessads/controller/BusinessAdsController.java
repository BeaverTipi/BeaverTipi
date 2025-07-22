package kr.or.ddit.admin.businessads.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.inject.Inject;
import kr.or.ddit.admin.businessads.service.BusinessAdsService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.BusinessAdsSearchVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/businessAds")
public class BusinessAdsController {

	@Inject
	private BusinessAdsService businessAdsService;

	@GetMapping("businessAdsList") // 기존 목록 조회
	public String selectBusinessAdsList(
	        @RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
	        @ModelAttribute("detailSearch") BusinessAdsSearchVO detailSearch,
	        Model model
	) {
		
		PaginationInfo<BoardVO> pagingVO = new PaginationInfo<>();
        pagingVO.setCurrentPageNo(currentPage);
        pagingVO.setDetailSearch(detailSearch);

		// 페이징을 위한 전체 레코드 수 조회 및 설정 (전체 게시글 수 조회)
	    int totalCount = businessAdsService.selectBusinessAdsCount(pagingVO);
	    pagingVO.setTotalRecordCount(totalCount);

	    // 현재 페이지 목록 조회
	    List<BoardVO> businessAdsList = businessAdsService.selectBusinessAdsList(pagingVO);

	    model.addAttribute("businessAdsList", businessAdsList);
	    model.addAttribute("pagingVO", pagingVO);

	    // 페이징 HTML 생성 및 모델에 추가
	    DefaultPaginationRenderer renderer = new DefaultPaginationRenderer();
	    String pagingHTML = renderer.renderPagination(pagingVO, "fn_paging");
	    model.addAttribute("pagingHTML", pagingHTML);

	    return "admin/businessAds/businessAdsList";
	}

	// 모달용 상세 정보 조회 메서드
	@GetMapping(value = "adsDetailModal.do")
	@ResponseBody
	public BoardVO selectBusinessAdsDetailForModal(@RequestParam("brdNo") String brdNo) {

		
		// 실제 서비스 메서드 호출
		BoardVO boardDetail = businessAdsService.selectBusinessAdsDetail(brdNo);

        return boardDetail; // 실제 DB에서 조회한 BoardVO 객체 반환
	}
	
	@PostMapping(value = "/updateAdsStatus.do", produces = MediaType.APPLICATION_JSON_VALUE) // produces 속성 -> 응답의 Content-Type 지정
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateAdsStatus(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>(); // Map 객체 생성
        try {
            String brdNo = payload.get("brdNo");
            String adsStatusCode = payload.get("adsStatusCode");

            log.info("updateAdsStatus - Received update request for brdNo: {}, new status: {}", brdNo, adsStatusCode);

            if (brdNo == null || brdNo.isEmpty() || adsStatusCode == null || adsStatusCode.isEmpty()) {
                response.put("success", false);
                response.put("message", "필수 파라미터(brdNo 또는 adsStatusCode)가 누락되었습니다.");
                log.warn("updateAdsStatus - Missing required parameters: brdNo={}, adsStatusCode={}", brdNo, adsStatusCode);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request
            }

            int result = businessAdsService.updateAdsStatus(brdNo, adsStatusCode);

            if (result > 0) {
                response.put("success", true);
                response.put("message", "광고 상태가 성공적으로 업데이트되었습니다.");
                log.info("updateAdsStatus - Successfully updated ad status for brdNo: {}", brdNo);
                return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK
            } else {
                response.put("success", false);
                response.put("message", "광고 상태 업데이트에 실패했습니다. (영향받은 행 없음)");
                log.warn("updateAdsStatus - Failed to update ad status for brdNo {} (no rows affected)", brdNo);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            }
        } catch (Exception e) {
            log.error("updateAdsStatus - 서버 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "서버 오류 발생: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }
	
	@GetMapping(value = "/approvedAds", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<BoardVO>> getApprovedAdsForMain() {
        try {
            List<BoardVO> approvedAds = businessAdsService.selectApprovedAdsForMain();
            if (approvedAds != null && !approvedAds.isEmpty()) {
                log.info("메인 페이지용 승인된 광고 {}개 조회 성공.", approvedAds.size());
                return new ResponseEntity<>(approvedAds, HttpStatus.OK);
            } else {
                log.info("메인 페이지용 승인된 광고가 없습니다.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
            }
        } catch (Exception e) {
            log.error("메인 페이지용 승인된 광고 조회 중 오류 발생: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }
}