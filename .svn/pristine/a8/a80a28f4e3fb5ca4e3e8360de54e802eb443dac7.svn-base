package kr.or.ddit.admin.business.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.admin.business.service.BusinessApproveService;
import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.vo.BusinessApproveSearchVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/business")
@RequiredArgsConstructor
@Slf4j
public class BusinessApproveController {
	private final BusinessApproveService service;
	private final CommonCodeService commonService;
	private final String MODELNAME = "search";
	
	@ModelAttribute(MODELNAME)
	public BusinessApproveSearchVO search() {
		return new BusinessApproveSearchVO();
	}
	@GetMapping("/approve")
	public String UI(
			Model model
			, @RequestParam(required = false, defaultValue = "1") int page
			, @ModelAttribute(MODELNAME) BusinessApproveSearchVO search
	) {
		log.info("=====> role정보 : {}",search.getRole());
		// 1. 페이징 객체 준비
				PaginationInfo<BusinessApproveSearchVO> paging = new PaginationInfo<>();
				paging.setCurrentPageNo(page);
				paging.setDetailSearch(search);
				
				// 2. 전체 개수 조회
				int totalRecord = service.readTotalRecord(paging);
				paging.setTotalRecordCount(totalRecord);

				// 3. 목록 조회
				List<SolutionSubscriptionVO> approveList = service.readBusinessApproveList(paging);

				// 4. 페이징 HTML 생성
				String pagingHTML = new DefaultPaginationRenderer().renderPagination(paging, "fnPaging");

				List<CommonCodeVO> roleList = commonService.readCommonCodeList("SOL");
				List<CommonCodeVO> statusCodeList = commonService.readCommonCodeList("APST");
				// 5. 모델 바인딩
				model.addAttribute("approveList", approveList);
				model.addAttribute("pagingHTML", pagingHTML);
				model.addAttribute("pagingInfo", paging);
				model.addAttribute("roleList", roleList);
				model.addAttribute("statusCodeList", statusCodeList);

				return "admin/business/businessApprove";
		}
}

