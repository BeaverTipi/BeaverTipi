package kr.or.ddit.resident.complaint.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.resident.complaint.service.ComplaintService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;
import kr.or.ddit.vo.UnitResidentVO;

@Controller
@RequestMapping("/resident/complaint")
public class ComplaintCreateController {

    @Autowired
    private ComplaintService complaintService;
    
    
    @Autowired
    private CommonCodeService commonCodeService;

    @Autowired
    private UnitResidentService unitResidentService;

    /**
     * 1) 등록·수정 폼
     */
    @GetMapping("/form")
    public String form(
            @RequestParam(value = "rsdBrdId", required = false) String rsdBrdId,
            @RequestParam(value = "bldgIdParam", required = false) String bldgIdParam,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            Model model
    ) {
        // 1-1) 로그인 사용자 입주 유닛 조회
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
        if (units.isEmpty()) {
            // 단지 입주자가 아닐 경우 회원가입 페이지로
            return "redirect:/member/register";
        }

        // 1-2) 선택된 건물 결정
        String selectedBldg = (bldgIdParam != null && !bldgIdParam.isBlank())
                ? bldgIdParam
                : units.get(0).getBldgId();

        List<CommonCodeVO> openYnList    = commonCodeService.readCommonCodeList("OPYN");
        List<CommonCodeVO> reqStatusList = commonCodeService.readCommonCodeList("PROC");
        // 모델에 공통으로 필요한 데이터
        model.addAttribute("unitList", units);
        model.addAttribute("selectedBldgId", selectedBldg);
        model.addAttribute("openYnList", openYnList);
        model.addAttribute("reqStatusList", reqStatusList);

        // 1-3) 수정 vs 등록 분기
        ResidentBoardVO vo;
        if (rsdBrdId != null && !rsdBrdId.isBlank()) {
            vo = complaintService.selectComplaintById(rsdBrdId);
        } else {
            vo = new ResidentBoardVO();
            vo.setBrdCode("M0001");             // 민원 게시판 코드
            vo.setBldgId(selectedBldg);
        }
        model.addAttribute("complaint", vo);

        return "resident/complaint/ComplaintForm";
    }

    /**
     * 2) 저장 처리 (등록 ↔ 수정 자동 분기)
     */
    @PostMapping("/save")
    @ResponseBody
    public String save(
            @ModelAttribute("complaint") ResidentBoardVO complaint,
            @RequestParam(value = "bldgIdParam", required = false) String bldgIdParam,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            Model model
    ) {
        // 작성자 설정 (인증된 사용자)
//        complaint.setMbrCd(principal.getRealUser().getMbrCd());
    	String loginMbrCd = principal.getRealUser().getMbrCd();

        // 신규 등록
        if (complaint.getRsdBrdId() == null || complaint.getRsdBrdId().isBlank()) {
            String nextId = complaintService.getNextComplaintId();
            complaint.setRsdBrdId(nextId);
            complaint.setBrdCode("M0001");
            complaint.setMbrCd(loginMbrCd);
            complaint.setReqStatus("001");
            complaintService.insertComplaint(complaint);
        }
        // 기존 글 수정
        else {
        	ResidentBoardVO original = complaintService.selectComplaintById(complaint.getRsdBrdId());
        	
        	if (!original.getMbrCd().equals(loginMbrCd)) {
				return "<script>alert('작성자 본인만 수정할 수 있습니다.'); history.back();</script>";
			}
        	
        	complaint.setMbrCd(loginMbrCd);
            complaintService.updateComplaint(complaint);
        }

        return "<script>location.href='/resident/complaint/view?rsdBrdId=" 
        + complaint.getRsdBrdId() + "&bldgIdParam=" + bldgIdParam + "';</script>";
    }
    @PostMapping("/delete")
    @ResponseBody
    public String delete(
        @RequestParam("rsdBrdId") String rsdBrdId,
        @RequestParam(value = "bldgIdParam", required = false) String bldgIdParam,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        // Optional: 본인 글 검증
        ResidentBoardVO vo = complaintService.selectComplaintById(rsdBrdId);
        if (!vo.getMbrCd().equals(principal.getRealUser().getMbrCd())) {
            return "<script>alert('작성자 본인만 삭제할 수 있습니다.'); history.back();</script>";
        }
        complaintService.deleteComplaint(rsdBrdId);
        return "<script>location.href='/resident/complaint?bldgIdParam=" + bldgIdParam + "';</script>";
    }

    

		
	
}
