package kr.or.ddit.admin.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.admin.board.service.NoticePostService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Controller
@RequestMapping("/admin")
public class NoticePostListController {
	
	@Autowired
	private NoticePostService service;
	
	@GetMapping("/notice/list")
	public String noticeList(
		@RequestParam(name="Page", defaultValue = "1")	int page,
		Model model
	) {
		PaginationInfo<BoardVO> paging = new PaginationInfo<>();
		paging.setCurrentPageNo(page);
		
		int totalRecord = service.getTotalNoticeRecord(paging);
		paging.setTotalRecordCount(totalRecord);
		
		List<BoardVO> boardList = service.readNoticeList(paging);
		
		model.addAttribute("boardList", boardList);
		
		return "admin/notice/adminNoticeList"; 
	}
}
