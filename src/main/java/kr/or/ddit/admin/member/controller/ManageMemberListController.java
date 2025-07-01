package kr.or.ddit.admin.member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String listHandler(@RequestParam(required = false) Map<String, Object> paramMap,
	                          Model model) {
	    List<MemberVO> memberList = service.readMemberList(paramMap);
	    model.addAttribute("memberList", memberList);
	    return "admin/memberManagement/memberList";
	}
	

}  

