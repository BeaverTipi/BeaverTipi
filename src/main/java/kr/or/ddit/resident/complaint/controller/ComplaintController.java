package kr.or.ddit.resident.complaint.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.resident.complaint.service.ComplaintService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.renderer.PaginationRenderer;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;
import kr.or.ddit.vo.UnitResidentVO;

@Controller
@RequestMapping("/resident/complaint")
public class ComplaintController {

	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private CommonCodeService codeService;
	
	@Autowired
	private UnitResidentService unitResidentService;
	
	@Autowired
	private PaginationRenderer paginationRenderer;
	
	@GetMapping
	public String complainList(
			    Model model,
		        @RequestParam(value="page", required=false, defaultValue="1") int page,
		        @RequestParam(value="bldgIdParam", required=false) String bldgIdParam,
		        @ModelAttribute("search") SimpleSearch simpleSearch,
		        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
			) { 
			simpleSearch.setBrdCode("M0001");
		
			MemberVO member = principal.getRealUser();
	        List<UnitResidentVO> units = 
	                unitResidentService.getUnitsByMember(member.getMbrCd());
	            if(units == null || units.isEmpty()) {
	                return "redirect:/member/register";
	            }

	            // 1-2) 선택된 건물 결정 (파라미터 우선, 없으면 가장 오래된 이동일 기준)
	            String selectedBldg = bldgIdParam;
	            if(selectedBldg == null || selectedBldg.isBlank()) {
	                selectedBldg = units.stream()
	                                     .min(Comparator.comparing(UnitResidentVO::getMoveInDt))
	                                     .map(UnitResidentVO::getBldgId)
	                                     .orElse(units.get(0).getBldgId());
	            }
	            simpleSearch.setBldgId(selectedBldg);
	            // 1-3) 공통코드 (검색폼 드롭다운)
	            List<CommonCodeVO> openYnList    = codeService.readCommonCodeList("OPEN_YN");
	            List<CommonCodeVO> reqStatusList = codeService.readCommonCodeList("REQ_STATUS");
	            model.addAttribute("openYnList",    openYnList);
	            model.addAttribute("reqStatusList", reqStatusList);

	            int totalCount = complaintService.selectComplaintCount(simpleSearch);
	            // 1-4) 페이징 & 검색
	            PaginationInfo pagingInfo = new PaginationInfo<>();
	            pagingInfo.setCurrentPageNo(page);
	            pagingInfo.setPageSize(10);
	            pagingInfo.setSimpleSearch(simpleSearch);
	            pagingInfo.setTotalRecordCount(totalCount);
	            
	            model.addAttribute("pagingInfo", pagingInfo);
	            // 1-5) 페이징 HTML
	            String pagingHtml = 
	                paginationRenderer.renderPagination(pagingInfo, "fnPaging");
	            model.addAttribute("pagingHtml", pagingHtml);

	            // 1-6) 데이터 조회
	            List<ResidentBoardVO> list = 
	                complaintService.selectComplaintList(simpleSearch, pagingInfo);
	            
	            model.addAttribute("boardList", list);
	            model.addAttribute("unitList",   units);
	            model.addAttribute("selectedBldgId", selectedBldg);
	            model.addAttribute("search", simpleSearch);

	            return "resident/complaint/ComplaintList";
	        }
			@GetMapping("/view")
			  public String view(
			      @RequestParam("rsdBrdId") String rsdBrdId,
			      @RequestParam(value="bldgIdParam", required=false) String bldgIdParam,
			      @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
			      Model model
			  ) {
			    // (optional) 본인글 체크
			    ResidentBoardVO vo = complaintService.selectComplaintById(rsdBrdId);
			    model.addAttribute("complaint", vo);
			    model.addAttribute("bldgIdParam", bldgIdParam);
			    return "resident/complaint/ComplaintDetail";
			  }


}
