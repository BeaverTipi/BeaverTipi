package kr.or.ddit.admin.board.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.admin.board.service.NoticePostService;
import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.util.validate.UpdateGroup;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice/form")
public class NoticePostFormController {

	private final NoticePostService service;
	private final CommonCodeService commonCode;

	static final String MODELNAME = "board";

	@ModelAttribute(MODELNAME)
	public BoardVO prepareBoard(
			@AuthenticationPrincipal(expression = "realUser") MemberVO authMember,  
			Model model,
            @RequestParam(name = "brdNo", required = false) String brdNo
    ) {
		if (model.containsAttribute(MODELNAME)) {
			return (BoardVO) model.getAttribute(MODELNAME);
		}

		BoardVO board;
		if (brdNo != null && !brdNo.isBlank()) {
			board = service.readBoard(brdNo);

			if (board.getNotice() != null && !board.getNotice().isEmpty()) {
				board.setNoticeType(board.getNotice().get(0).getNoticeType());
				board.setBrdEndDtm(board.getNotice().get(0).getNoticeEndDtm());
			}

			if (board.getFaq() != null && !board.getFaq().isEmpty()) {
				board.setFaqCtgry(board.getFaq().get(0).getFaqCtgry());
			}

			if (board.getQna() != null && !board.getQna().isEmpty()) {
				board.setQnaCtgry(board.getQna().get(0).getQnaCtgry());
				board.setQnaStatus(board.getQna().get(0).getQnaStatus());
				board.setAnswerCont(board.getQna().get(0).getAnswerCont());
			}
		} else {
			board = new BoardVO();
			board.setBrdCode("S0001");
			board.setBrdCtgryGrpCd("BRDCT");
			if (authMember != null) {
				board.setMbrCd(authMember.getMbrCd());
				board.setAnswerAdminId(authMember.getMbrCd());
			}
		}
		return board;
	}

	@GetMapping
	public String noticeForm(
			@ModelAttribute(MODELNAME) BoardVO board,
			Model model
	) {
		if (board.getBrdNo() != null && !board.getBrdNo().isBlank()) {
			model.addAttribute("pageTitle", "공지사항 수정");
		} else {
			model.addAttribute("pageTitle", "새 공지사항 등록");
		}

		model.addAttribute("brdCodeList", commonCode.readCommonCodeList("BRDCT"));
		model.addAttribute("noticeTypeList", commonCode.readCommonCodeList("NOTPE"));
		model.addAttribute("faqCtgryList", commonCode.readCommonCodeList("FAQCT"));
		model.addAttribute("qnaCtgryList", commonCode.readCommonCodeList("QNACT"));
		return "admin/notice/adminNoticeForm";
	}

	@PostMapping("insert")
	public String noticeWriteSubmit(@Validated(InsertGroup.class) @ModelAttribute("board") BoardVO board,
			BindingResult errors, RedirectAttributes redirectAttributes) {
		String lvn;
		if (!errors.hasErrors()) {
			int result = service.createBoard(board);
			System.out.println("▶ 새 글 등록 결과: " + result);
			lvn = "redirect:/admin/notice/list";
		} else {
			System.out.println("▶ 오류 발생: " + errors);
			redirectAttributes.addFlashAttribute(MODELNAME, board);
			redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + MODELNAME, errors);
			lvn = "redirect:/admin/notice/form";
		}
		return lvn;
	}
	
	@PostMapping("update")
	public String noticeUpdateSubmit(@Validated(UpdateGroup.class) @ModelAttribute("board") BoardVO board,
			BindingResult errors, RedirectAttributes redirectAttributes) {
		String lvn;
		if (!errors.hasErrors()) {
			int result = service.modifyBoard(board);
			System.out.println("▶ 기존 글 수정 결과: " + result);
			lvn = "redirect:/admin/detail/notice?brdNo="+board.getBrdNo();
		} else {
			System.out.println("▶ 오류 발생: " + errors);
			redirectAttributes.addFlashAttribute(MODELNAME, board);
			redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + MODELNAME, errors);
			lvn = "redirect:/admin/notice/form?brdNo=" + board.getBrdNo();
		}
		return lvn;
	}
}
