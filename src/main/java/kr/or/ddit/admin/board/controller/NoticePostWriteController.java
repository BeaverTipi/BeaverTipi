package kr.or.ddit.admin.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.admin.board.service.NoticePostService;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.NoticeVO;

@Controller
@RequestMapping("/admin")
public class NoticePostWriteController {
	
	@GetMapping("/notice/write")
	public String noticewriteForm(Model model) {
		BoardVO board = new BoardVO();
		board.setBrdCode("공지사항");
		
		NoticeVO notice = new NoticeVO();
		notice.setNoticeTop("Y");
		List<NoticeVO> noticeList = new ArrayList<>();
		noticeList.add(notice);
		
		board.setNotice(noticeList);
		
		model.addAttribute("board", board);
		model.addAttribute("pageTitle","새 공지사항 등록");
		return "admin/notice/noticeForm";
	}
}
