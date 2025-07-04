package kr.or.ddit.admin.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.admin.board.service.NoticePostService;
import kr.or.ddit.vo.BoardVO;

@Controller
@RequestMapping("/admin")
public class NoticePostListController {
	
	@Autowired
	private NoticePostService service;
	
	@GetMapping("/notice/list")
	public String noticeList(Model model) {
		
		List<BoardVO> boardList = service.readNoticeList();
		
		model.addAttribute("boardList", boardList);
		
		return "admin/notice/adminNoticeList"; 
	}
}
