package kr.or.ddit.admin.board.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.admin.board.service.NoticePostService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticePostListController {

	private final NoticePostService service;

	@GetMapping("/list")
	public String boardList(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "tab", defaultValue = "notice") String tab,
			@RequestParam(name = "ctgryCode", required = false) String ctgryCode,
			@RequestParam(name = "statusCode", required = false) String statusCode,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate, Model model) {
		
		String brdCode = switch (tab) {
		case "notice" -> "S0001";
		case "qna" -> "S0002";
		case "faq" -> "S0003";
		default -> "S0001";
		};

		PaginationInfo<BoardVO> paging = new PaginationInfo<>();
		paging.setCurrentPageNo(page);

		BoardVO condition = new BoardVO();
		condition.setBrdCode(brdCode);
		condition.setBrdTitlNm(keyword);
		condition.setBrdCont(keyword);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		if (startDate != null && !startDate.isBlank()) {
			condition.setBrdPblsDtmStart(LocalDate.parse(startDate, formatter));
		}
		if (endDate != null && !endDate.isBlank()) {
			condition.setBrdPblsDtmEnd(LocalDate.parse(endDate, formatter));
		}

		if ("faq".equals(tab)) {
			condition.setFaqCtgry(ctgryCode);
		} else if ("qna".equals(tab)) {
			condition.setQnaCtgry(ctgryCode);
			condition.setQnaStatus(statusCode);
		}
		paging.setDetailSearch(condition);

		int totalRecord = service.getTotalBoardRecord(paging);
		paging.setTotalRecordCount(totalRecord);
		List<BoardVO> boardList = service.readBoardList(paging);

		model.addAttribute("boardList", boardList);
		model.addAttribute("paging", paging);
		model.addAttribute("codeValue", brdCode);
		model.addAttribute("activeTab", tab);
		
		System.out.println(">>> brdCode = " + brdCode);
		
		return "admin/notice/adminNoticeList";
	}

}
