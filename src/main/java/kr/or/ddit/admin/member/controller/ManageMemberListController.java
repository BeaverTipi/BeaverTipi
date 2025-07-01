package kr.or.ddit.admin.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.admin.member.service.ManageMemberService;
import kr.or.ddit.vo.MemberVO;

@Controller
@RequestMapping("/admin")
public class ManageMemberListController {
	private ManageMemberService service;
	
	@Autowired
	public void setService(ManageMemberService service) {
		this.service = service;
	}
	
	
	@GetMapping("/member/list")
	public void listHandler(Model model) {
		List<MemberVO> memberList = service.readMemberList();
		model.addAttribute("memberList", memberList);
	}
	
//	@GetMapping("/member/list")
//	public String cerificateList() {
//		return "admin/memberManagement/memberList";
//	}
}  

