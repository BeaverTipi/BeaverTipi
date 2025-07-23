package kr.or.ddit.admin.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kr.or.ddit.admin.board.service.NoticePostService;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.MemberVO;

@Controller
@RequestMapping("/admin/notice/write")
public class NoticePostWriteController {

	@Autowired
	private NoticePostService service;

	static final String MODELNAME = "board";

	@ModelAttribute("board")
	public BoardVO prepareBoard(HttpSession session) {
		BoardVO board = new BoardVO();
		board.setBrdCode("007");
		return board;
	}

	@InitBinder("board")
	public void initBinder(WebDataBinder binder,
			@AuthenticationPrincipal(expression = "realUser") MemberVO authMember) {
		Object target = binder.getTarget();
		if (target instanceof BoardVO && authMember != null) {
			BoardVO board = (BoardVO) target;
			if (board.getMbrCd() == null || board.getMbrCd().isBlank()) {
				board.setMbrCd(authMember.getMbrCd());
			}
		}
	}

	@GetMapping
	public String noticewriteForm(Model model) {
		model.addAttribute("pageTitle", "새 공지사항 등록");
		return "admin/notice/adminNoticeForm";
	}

	@PostMapping
	public String noticeWriteSubmit(
			@Validated(InsertGroup.class) @ModelAttribute("board") BoardVO board,
			BindingResult errors, 
			RedirectAttributes redirectAttributes
	) {
		String lvn;
		if (!errors.hasErrors()) {
			int result = service.createBoard(board);
			System.out.println("▶ 저장 결과: " + result);
			lvn = "redirect:/admin/notice/list";
		} else {
			System.out.println("▶ 오류 발생: " + errors);
			redirectAttributes.addFlashAttribute(MODELNAME, board);
			redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + MODELNAME, errors);
			lvn = "redirect:/admin/notice/write";
		}
		return lvn;
	}
}
