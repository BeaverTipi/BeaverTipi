package kr.or.ddit.admin.member.controller;

import java.util.List;
// import java.util.Map; // Map 임포트는 이제 필요 없습니다.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.admin.member.service.ManageMemberService;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.SearchConditionVO;

@Controller
@RequestMapping("/admin")
public class ManageMemberListController {
    private ManageMemberService service;

    @Autowired
    public void setService(ManageMemberService service) {
        this.service = service;
    }

    @GetMapping("/member/list")
    public String listHandler(@ModelAttribute("searchCondition") SearchConditionVO searchCondition,
                              Model model) {
    	System.out.println("Model Attributes: " + model.asMap());
        // 이전에는 SearchConditionVO를 Map으로 변환하는 로직이 있었지만,
        // 이제 서비스 계층이 SearchConditionVO를 직접 받으므로 이 부분은 필요 없습니다.
        // 서비스 계층으로 SearchConditionVO 객체를 직접 전달합니다.
        List<MemberVO> memberList = service.readMemberList(searchCondition);
        
        model.addAttribute("memberList", memberList);
        
        return "admin/memberManagement/memberList";
    }
}