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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.resident.complaint.service.ComplaintService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentBoardVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident/complaint")
public class ComplaintCreateController {

    @Autowired
    private ComplaintService complaintService;
    
    
    @Autowired
    private CommonCodeService commonCodeService;

    @Autowired
    private UnitResidentService unitResidentService;

    @ModelAttribute("complaint")
    public ResidentBoardVO prepareComplaint(
            @RequestParam(value = "rsdBrdId", required = false) String rsdBrdId,
            @RequestParam(value = "bldgIdParam", required = false) String bldgIdParam,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

        if (rsdBrdId != null && !rsdBrdId.isBlank()) {
            ResidentBoardVO vo = complaintService.selectComplaintById(rsdBrdId);
            return vo != null ? vo : new ResidentBoardVO();
        } else {
            ResidentBoardVO newComplaint = new ResidentBoardVO();
            if (bldgIdParam != null && !bldgIdParam.isBlank()) {
                newComplaint.setBldgId(bldgIdParam);
            }
            return newComplaint;
        }
    }
    
    /**
     * 1) 등록·수정 폼
     */
    @GetMapping("/form")
    public String form(
            @RequestParam(value = "rsdBrdId", required = false) String rsdBrdId,
            @RequestParam(value = "bldgIdParam", required = false) String bldgIdParam,
            @RequestParam(value = "bldgId", required = false) String bldgId, 
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            @ModelAttribute("complaint") ResidentBoardVO complaint,
            Model model
    ) {
        // 1-1) 로그인 사용자 입주 유닛 조회
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());

        // 로그 추가
        log.info("[form] units size: {}", units.size());  // unitList 사이즈 로그

        if (units.isEmpty()) {
            // 단지 입주자가 아닐 경우 회원가입 페이지로
            return "redirect:/member/register";
        }
        
        String selectedBldg = null;
        if (bldgIdParam != null && !bldgIdParam.isBlank()) {
            selectedBldg = bldgIdParam;
        } else if (bldgId != null && !bldgId.isBlank()) {
            selectedBldg = bldgId;
        } else if (complaint.getBldgId() != null && !complaint.getBldgId().isBlank()) {
            selectedBldg = complaint.getBldgId();
        } else {
            selectedBldg = units.get(0).getBldgId();
        }

        complaint.setBldgId(selectedBldg);
        
        log.info("🏢 선택된 건물 ID: " + selectedBldg);
        log.info("🏢 bldgIdParam: " + bldgIdParam);
        
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

            // ✅ 처리완료된 민원은 수정할 수 없음
            if ("002".equals(vo.getReqStatus())) {
                model.addAttribute("error", "처리완료된 민원은 수정할 수 없습니다.");
                return "redirect:/resident/complaint/view?rsdBrdId=" + rsdBrdId + "&bldgIdParam=" + bldgIdParam;
            }
        } else {
            vo = new ResidentBoardVO();
            vo.setBrdCode("M0001");
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
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            RedirectAttributes ra, Model model
    ) {
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());

        if (units.isEmpty()) {
            model.addAttribute("unitList", units);
            model.addAttribute("error", "입주 정보가 없습니다.");
            return "resident/complaint/ComplaintForm";
        }

        // 건물 정보 확인
        if (complaint.getBldgId() == null || complaint.getBldgId().isBlank()) {
            model.addAttribute("unitList", units);
            model.addAttribute("error", "건물을 선택해주세요.");
            return "resident/complaint/ComplaintForm";
        }

        complaint.setMbrCd(member.getMbrCd());
        complaint.setBrdCode("M0001"); // 민원 게시판 코드 고정

        // 신규 등록/수정 처리
        if (complaint.getRsdBrdId() == null || complaint.getRsdBrdId().isEmpty()) {
            complaintService.insertComplaint(complaint);
            ra.addFlashAttribute("saveMsg", "등록되었습니다.");
        } else {
        	
        	ResidentBoardVO residentBoardVO = complaintService.selectComplaintById(complaint.getRsdBrdId());
        	if("002".equals(residentBoardVO.getReqStatus())) {
        		ra.addFlashAttribute("error", "처리가 완료된 민원은 수정할 수 없습니다,");
        	    return "redirect:/resident/complaint/detail?rsdBrdId=" + complaint.getRsdBrdId() + "&bldgIdParam=" + bldgIdParam;
            }
        	
            complaintService.updateComplaint(complaint);
            ra.addFlashAttribute("saveMsg", "수정되었습니다.");
        }

        return "redirect:/resident/complaint?bldgIdParam=" + bldgIdParam;
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
