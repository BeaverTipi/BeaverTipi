package kr.or.ddit.admin.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.admin.board.service.NoticePostService;
import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice/write")
public class NoticePostWriteController {

	private final NoticePostService service;
	private final CommonCodeService commonCode;

	static final String MODELNAME = "board";

	@ModelAttribute(MODELNAME)
	public BoardVO prepareBoard(@AuthenticationPrincipal(expression = "realUser") MemberVO authMember) {
		BoardVO board = new BoardVO();
		board.setBrdCode("007");
		board.setBrdCtgryGrpCd("BRDCT"); 
		if (authMember != null) {
			board.setMbrCd(authMember.getMbrCd());
		}
		return board;
	}

	@GetMapping
	public String noticeWriteForm(Model model) {
		model.addAttribute("pageTitle", "새 공지사항 등록");
		model.addAttribute("brdCodeList", commonCode.readCommonCodeList("BRDCT"));
		model.addAttribute("noticeTypeList", commonCode.readCommonCodeList("NOTPE"));
		model.addAttribute("faqCtgryList", commonCode.readCommonCodeList("FAQCT"));
		model.addAttribute("qnaCtgryList", commonCode.readCommonCodeList("QNACT"));
		return "admin/notice/adminNoticeForm";
	}

	@PostMapping
	public String noticeWriteSubmit(@Validated(InsertGroup.class) @ModelAttribute("board") BoardVO board,
			BindingResult errors, RedirectAttributes redirectAttributes) {
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
