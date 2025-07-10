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

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.resident.complaint.service.ComplaintService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
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

        // 모델에 공통으로 필요한 데이터
        model.addAttribute("unitList", units);
        model.addAttribute("selectedBldgId", selectedBldg);
        model.addAttribute("openYnList", commonCodeService.readCommonCodeList("OPEN_YN"));
        model.addAttribute("reqStatusList", commonCodeService.readCommonCodeList("REQ_STATUS"));

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
    public String save(
            @ModelAttribute("complaint") ResidentBoardVO complaint,
            @RequestParam(value = "bldgIdParam", required = false) String bldgIdParam,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        // 작성자 설정 (인증된 사용자)
        complaint.setMbrCd(principal.getRealUser().getMbrCd());

        // 신규 등록
        if (complaint.getRsdBrdId() == null || complaint.getRsdBrdId().isBlank()) {
            String nextId = complaintService.getNextComplaintId();
            complaint.setRsdBrdId(nextId);
            complaint.setBrdCode("M0001");
            complaintService.insertComplaint(complaint);
        }
        // 기존 글 수정
        else {
            complaintService.updateComplaint(complaint);
        }

        return "redirect:/resident/complaint?bldgIdParam=" + bldgIdParam;
    }
    @PostMapping("/delete")
    public String delete(
        @RequestParam("rsdBrdId") String rsdBrdId,
        @RequestParam(value = "bldgIdParam", required = false) String bldgIdParam,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        // Optional: 본인 글 검증
        ResidentBoardVO vo = complaintService.selectComplaintById(rsdBrdId);
        if (!vo.getMbrCd().equals(principal.getRealUser().getMbrCd())) {
            return "redirect:/accessDenied";
        }
        complaintService.deleteComplaint(rsdBrdId);
        return "redirect:/resident/complaint?bldgIdParam=" + bldgIdParam;
    }

    

		
	
}
