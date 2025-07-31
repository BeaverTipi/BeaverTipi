package kr.or.ddit.admin.board.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.admin.board.service.NoticePostService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/detail")
public class NoticePostDetailController {
	
	private final NoticePostService service;
	
	@GetMapping("notice")
	public String detailNoticeCont(@RequestParam("brdNo") String brdNo, Model model) {
		BoardVO board = service.readBoard(brdNo);
		model.addAttribute("board", board);
		return "admin/notice/adminNoticeDetail";
	}
	
	@GetMapping("qna")
	public String detailQnaCont(
		@RequestParam("brdNo") String brdNo,
		@RequestParam(value = "editAnswer", required = false) Boolean editAnswer,
		Model model,
		@AuthenticationPrincipal RealUserWrapper<MemberVO> principal
	) {
		BoardVO board = service.readBoard(brdNo);
		model.addAttribute("board", board);

		boolean isAdmin = false;
		if (principal != null && principal.getRealUser() != null) {
			MemberVO loginUser = principal.getRealUser();
			log.info("💡 로그인 사용자: {}", loginUser);
			isAdmin = "admin".equalsIgnoreCase(loginUser.getMbrId());
			model.addAttribute("loginMember", loginUser);
		}

		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("editAnswer", editAnswer != null && editAnswer); // JSP에서 활용

		return "admin/notice/adminQnaDetail";
	}

	@PostMapping("/qnaAnswer")
	public String submitQnaAnswer(
			BoardVO board,
	        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
	        RedirectAttributes redirectAttributes
	) {
		if (principal != null && principal.getRealUser() != null) {
			board.setAnswerAdminId(principal.getRealUser().getMbrId());
		}
		int updated = service.updateQna(board);
		if (updated > 0) {
			redirectAttributes.addFlashAttribute("message", "답변이 등록되었습니다.");
		} else {
			redirectAttributes.addFlashAttribute("message", "답변 등록에 실패했습니다.");
		}
		return "redirect:/admin/detail/qna?brdNo=" + board.getBrdNo();
	}


}
