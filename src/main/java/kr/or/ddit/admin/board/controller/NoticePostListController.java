package kr.or.ddit.admin.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.admin.board.service.NoticePostService;

@Controller
@RequestMapping("/admin")
public class NoticePostListController {
	
	@GetMapping("/notice/list")
	public String noticeList() {
		 return "admin/notice/noticeList"; 
	}
}
