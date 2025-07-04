package kr.or.ddit.admin.member.controller;

import java.util.List;
import java.util.Map; // List<Map<String, String>>을 받을 경우 필요
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping; // ⭐ POST 매핑을 위해 추가 ⭐
import org.springframework.web.bind.annotation.RequestBody; // ⭐ JSON 요청 본문을 받기 위해 추가 ⭐
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody; // ⭐ JSON 응답을 위해 추가 (Controller 사용 시) ⭐

import kr.or.ddit.admin.member.service.ManageMemberService;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberSearchVO;

@Controller
@RequestMapping("/admin")
public class ManageMemberListController {
    private ManageMemberService service;

    @Autowired
    public void setService(ManageMemberService service) {
        this.service = service;
    }

    @GetMapping("/member/list")
    public String listHandler(@ModelAttribute("searchCondition") MemberSearchVO searchCondition,
                                  Model model) {
        if (searchCondition.getUserRoleId() != null && !searchCondition.getUserRoleId().isEmpty()) {
            searchCondition.setUserRoleCount(searchCondition.getUserRoleId().size());
        } else {
            searchCondition.setUserRoleCount(0);
        }
        
        List<MemberVO> memberList = service.readMemberList(searchCondition);
        
        model.addAttribute("memberList", memberList);
        model.addAttribute("searchCondition", searchCondition);
        
        return "admin/memberManagement/memberList";
    }

    // ⭐ 회원 상태 업데이트를 위한 새로운 POST 엔드포인트 ⭐
    @PostMapping("/member/updateStatus")
    @ResponseBody // 이 메서드의 반환 값이 HTTP 응답 본문에 직접 쓰여질 것임을 나타냅니다.
    public String updateMemberStatus(@RequestBody List<MemberVO> membersToUpdate) { // ⭐ List<MemberVO>로 받습니다 ⭐
        // 또는 @RequestBody List<Map<String, String>> membersToUpdate
        // 만약 MemberVO에 너무 많은 필드가 있어 부담스럽다면 Map으로 받을 수 있습니다.
        // 이때 Map의 키는 JSON의 키(mbrCd, mbrStatusCode)와 일치해야 합니다.

        try {
            int successCount = 0;
            for (MemberVO member : membersToUpdate) {
                // 각 MemberVO 객체에서 mbrCd와 mbrStatusCode를 사용하여 업데이트
                // 서비스 계층의 updateMemberStatus 메서드를 호출 (아래 4번에서 추가 예정)
                service.updateMemberStatus(member.getMbrCd(), member.getMbrStatusCode());
                successCount++;
            }
            return successCount + "명의 회원 상태가 성공적으로 업데이트되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "회원 상태 업데이트 중 오류 발생: " + e.getMessage();
        }
    }
}