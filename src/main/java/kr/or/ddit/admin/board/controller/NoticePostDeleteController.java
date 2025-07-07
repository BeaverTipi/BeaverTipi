package kr.or.ddit.admin.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class NoticePostDeleteController {
	
	@GetMapping("/notice/delete")
	public String noticeDelete() {
		return "admin/notice/adminNoticeDelete";
	}
}
