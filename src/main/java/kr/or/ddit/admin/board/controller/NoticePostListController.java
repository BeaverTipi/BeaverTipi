package kr.or.ddit.admin.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.admin.board.service.NoticePostService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticePostListController {

	private final NoticePostService service;

	@ModelAttribute("board")
	public BoardVO boardCondition(
			@RequestParam(name = "tab", defaultValue = "notice") String tab,
			@RequestParam(name = "ctgryCode", required = false) String ctgryCode,
			@RequestParam(name = "statusCode", required = false) String statusCode,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate
	) {
		return service.resolveBoardCondition(tab, ctgryCode, statusCode, keyword, startDate, endDate);
	}

	@GetMapping("/list")
	public String boardList(
			@RequestParam(name = "page", defaultValue = "1") int page,
			@ModelAttribute("board") BoardVO condition,
			@RequestParam(name = "tab", defaultValue = "notice") String tab,
			Model model) {

		PaginationInfo<BoardVO> paging = new PaginationInfo<>();
		paging.setCurrentPageNo(page);
		paging.setDetailSearch(condition);

		int totalRecord = service.getTotalBoardRecord(paging);
		paging.setTotalRecordCount(totalRecord);
		List<BoardVO> boardList = service.readBoardList(paging);

		model.addAttribute("boardList", boardList);
		model.addAttribute("paging", paging);
		model.addAttribute("codeValue", condition.getBrdCode());
		model.addAttribute("activeTab", tab); 

		return "admin/notice/adminNoticeList";
	}
	
	@PostMapping("/delete")
	public String deleteBoardPosts(
	        @RequestParam("brdNoList") List<String> brdNoList,
	        @RequestParam("tab") String tab,
	        RedirectAttributes redirectAttributes) {

	    if (brdNoList == null || brdNoList.isEmpty()) {
	        redirectAttributes.addFlashAttribute("message", "삭제할 게시글을 선택해주세요.");
	        return "redirect:/admin/notice/list?tab=" + tab;
	    }

	    int deletedCount = service.deleteBoardList(brdNoList);

	    redirectAttributes.addFlashAttribute("message", deletedCount + "건이 삭제되었습니다.");
	    return "redirect:/admin/notice/list?tab=" + tab;
	}

}
