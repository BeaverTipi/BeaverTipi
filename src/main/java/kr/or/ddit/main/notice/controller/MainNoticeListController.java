package kr.or.ddit.main.notice.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.main.notice.service.MainNoticeService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main/notice")
public class MainNoticeListController {

	private final MainNoticeService service;

	@GetMapping("/list")
	public String mainNoticeList(
			@RequestParam(name = "page", defaultValue = "1") int page,
			Model model) {

		PaginationInfo<BoardVO> paging = new PaginationInfo<>();
		paging.setCurrentPageNo(page);

		BoardVO condition = new BoardVO();
		condition.setBrdCode("S0001");
		paging.setDetailSearch(condition);

		int totalRecord = service.getTotalMainNoticeCount(paging);
		paging.setTotalRecordCount(totalRecord);

		List<BoardVO> noticeList = service.readBoardList(paging);

		model.addAttribute("noticeList", noticeList);
		model.addAttribute("paging", paging);

		return "main/notice/mainNoticeList";
		
	}

}
