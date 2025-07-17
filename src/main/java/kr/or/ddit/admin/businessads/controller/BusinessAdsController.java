package kr.or.ddit.admin.businessads.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.inject.Inject;
import kr.or.ddit.admin.businessads.service.BusinessAdsService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.vo.BoardVO;
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
	        @ModelAttribute("detailSearch") BoardVO detailSearch,
	        Model model
	) {
		// detailSearch.getAdsClient()가 null일 수 있으므로 초기화 필요
		if (detailSearch.getAdsClient() == null) {
			detailSearch.setAdsClient(new kr.or.ddit.vo.AdsClientVO()); //AdsClientVO 객체 초기화
		}
		
		
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
	@GetMapping(value = "adsDetailModal.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BoardVO selectBusinessAdsDetailForModal(@RequestParam("brdNo") String brdNo) {
		log.info("모달 상세 조회 요청 - brdNo: {}", brdNo);

		// 실제 서비스 메서드 호출
		BoardVO boardDetail = businessAdsService.selectBusinessAdsDetail(brdNo);

        return boardDetail; // 실제 DB에서 조회한 BoardVO 객체 반환
	}
}