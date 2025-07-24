package kr.or.ddit.resident.complaint.controller;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		        @RequestParam(value = "myPostsOnly", required = false)String myPostsOnly,
		        @ModelAttribute("search") SimpleSearch simpleSearch,
		        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
			) { 
			MemberVO member = principal.getRealUser();
			simpleSearch.setLoginMbrCd(member.getMbrCd());
			
			if (!"Y".equals(simpleSearch.getOpenYn()) && !"N".equals(simpleSearch.getOpenYn())) {
			    simpleSearch.setOpenYn(null); // 필터링 제거
			}
			
			
			simpleSearch.setBrdCode("M0001");
	        List<UnitResidentVO> units = 
	                unitResidentService.getUnitsByMember(member.getMbrCd());
	            if(units == null || units.isEmpty()) {
	                return "redirect:/member/register";
	            }

	            // 1-2) 선택된 건물 결정 (파라미터 우선, 없으면 가장 오래된 이동일 기준)
	            if(bldgIdParam == null || bldgIdParam.isBlank()) {
	            	bldgIdParam = units.stream()
	                                     .min(Comparator.comparing(UnitResidentVO::getMoveInDt))
	                                     .map(UnitResidentVO::getBldgId)
	                                     .orElse(units.get(0).getBldgId());
	            }
	            simpleSearch.setBldgId(bldgIdParam);
	            simpleSearch.setMyPostsOnly("Y".equalsIgnoreCase(myPostsOnly));
	            
	            PaginationInfo<ResidentBoardVO> pagingInfo = new PaginationInfo<>();
	            pagingInfo.setCurrentPageNo(page);
	            pagingInfo.setPageSize(10);
	            pagingInfo.setSimpleSearch(simpleSearch);
	            
	            Map<String, Object> paramMap = new HashMap<>();
	            paramMap.put("search",simpleSearch);
	            int totalCount = complaintService.selectComplaintCount(paramMap);
	            
	            // 1-3) 공통코드 (검색폼 드롭다운)
	            List<CommonCodeVO> openYnList    = codeService.readCommonCodeList("OPYN");
	            List<CommonCodeVO> reqStatusList = codeService.readCommonCodeList("PROC");
	            model.addAttribute("openYnList", openYnList);
	            model.addAttribute("reqStatusList", reqStatusList);
	            
	            pagingInfo.setTotalRecordCount(totalCount);
	            paramMap.put("paging", pagingInfo);
	            
	            // 1-4) 페이징 & 검색
	            
	            model.addAttribute("pagingInfo", pagingInfo);
	            // 1-5) 페이징 HTML
	            String pagingHtml = 
	                paginationRenderer.renderPagination(pagingInfo, "fnPaging");
	            model.addAttribute("pagingHtml", pagingHtml);

	            // 1-6) 데이터 조회
	            List<ResidentBoardVO> list = 
	                complaintService.selectComplaintList(paramMap);
	            
	            model.addAttribute("loginMember", member);
	            model.addAttribute("boardList", list);
	            model.addAttribute("unitList",   units);
	            model.addAttribute("selectedBldgId", bldgIdParam);
	            model.addAttribute("search", simpleSearch);
	            
	            
	            log.info("🔍 검색 조건: " , simpleSearch);
	            return "resident/complaint/ComplaintList";
	        }
	@GetMapping("/view")
	public String view(
	    @RequestParam("rsdBrdId") String rsdBrdId,
	    @RequestParam(value="bldgIdParam", required=false) String bldgIdParam,
	    @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
	    Model model
	) {
	    // 게시글과 답글 조회
	    MemberVO loginMember = principal.getRealUser();
	    
	    if(!complaintService.canViewComplaint(rsdBrdId, loginMember.getMbrCd())) {
	    	return "redirect:/resident/complaint?unauthorized=true";
	    }
	    
	    ResidentBoardVO vo = complaintService.selectComplaintById(rsdBrdId);
	    
	    boolean isLandlord = complaintService.isBuildingOwner(loginMember.getMbrCd(), vo.getBldgId());
	    
	    List<CommonCodeVO> openYnList = codeService.readCommonCodeList("OPYN");
	    List<CommonCodeVO> reqStatusList = codeService.readCommonCodeList("PROC");

	    log.info("로그인한 사용자 MBR_CD: {}", loginMember.getMbrCd());
	    log.info("isLandlord flag: {}", isLandlord);
	    log.info("불러온 민원 정보 - BLDG_ID: {}", vo.getBldgId());
	    log.info("isLandlord: {}", isLandlord);

	    model.addAttribute("openYnList", openYnList);
	    model.addAttribute("reqStatusList", reqStatusList);
	    model.addAttribute("isLandlord", isLandlord);
	    model.addAttribute("loginMember", loginMember);
	    model.addAttribute("complaint", vo);  // 게시글 정보
	    model.addAttribute("bldgIdParam", bldgIdParam);
	    return "resident/complaint/ComplaintDetail";  // 민원 상세보기 페이지로 리턴
	}
			
			@PostMapping("/reply")
			public String saveReply(
			        @RequestParam("rsdBrdId") String rsdBrdId,
			        @RequestParam("replyCont") String replyCont,
			        @RequestParam("bldgIdParam") String bldgIdParam,
			        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
			        RedirectAttributes ra
			) {
			    MemberVO landlord = principal.getRealUser();
			    
			    ResidentBoardVO vo = complaintService.selectComplaintById(rsdBrdId);
			    
			    // 권한 확인: 로그인한 사용자가 해당 건물의 임대인인지 체크
			    boolean isLandlord = complaintService.isBuildingOwner(landlord.getMbrCd(), vo.getBldgId());

			    if (!isLandlord) {
			        // 임대인이 아니면 권한 없음으로 처리
			        return "redirect:/resident/complaint/view?rsdBrdId=" + rsdBrdId + "&bldgIdParam=" + bldgIdParam + "&unauthorized=true";
			    }

			    // 댓글 작성
			    ResidentBoardVO reply = new ResidentBoardVO();
			    reply.setRsdBrdId(rsdBrdId);
			    reply.setReplyCont(replyCont);
			    reply.setReqStatus("002"); // 처리완료 상태로 설정
			    reply.setRsdBrdModDtm(LocalDateTime.now());

			    log.info("임대인 검증 결과: mbrCd={}, bldgId={}", landlord.getMbrCd(), bldgIdParam);
			    log.info("댓글 내용: {}", replyCont);

			    // 댓글 저장 처리
			    complaintService.replyToComplaint(reply);

			    ra.addFlashAttribute("success", "답변이 성공적으로 등록되었습니다.");
			    
			    // 답글이 포함된 민원 상세보기 페이지로 리다이렉트
			    return "redirect:/resident/complaint/view?rsdBrdId=" + rsdBrdId + "&bldgIdParam=" + bldgIdParam;
			}

}
