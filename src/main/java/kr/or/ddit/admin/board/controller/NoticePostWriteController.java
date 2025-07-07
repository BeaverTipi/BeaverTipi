package kr.or.ddit.admin.board.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.FAQVO;
import kr.or.ddit.vo.NoticeVO;
import kr.or.ddit.vo.QnAVO;

@Controller
@RequestMapping("/admin")
public class NoticePostWriteController {
	
	@Autowired
	private NoticePostService service;
	
	static final String MODELNAME = "board";
	
	@ModelAttribute(MODELNAME)
	public BoardVO board() {
		return new BoardVO();
	}
	
	@GetMapping("/notice/write")
	public String noticewriteForm(Model model) {
		BoardVO board = new BoardVO();
		board.setBrdCode("공지사항");
		
		NoticeVO notice = new NoticeVO();
		notice.setNoticeTop("Y");
		notice.setNoticeDelYn("N");
		List<NoticeVO> noticeList = new ArrayList<>();
		noticeList.add(notice);
		
		FAQVO faq = new FAQVO();
		faq.setFaqYn("Y");
		faq.setFaqCreDtm(LocalDate.now());
		
		List<FAQVO> faqList= new ArrayList<>();
		faqList.add(faq);
		
		QnAVO qna = new QnAVO();
		qna.setQnaYn("Y");
		qna.setQnaCreDtm(LocalDate.now());
		
		List<QnAVO> qnaList = new ArrayList<>();
		qnaList.add(qna);
		
		board.setNotice(noticeList);
		board.setFaq(faqList);
		board.setQna(qnaList);
		
		model.addAttribute("board", board);
		model.addAttribute("pageTitle","새 공지사항 등록");
		return "admin/notice/adminNoticeForm";
	}
	
	@PostMapping("/notice/write") 
	public String noticeWriteSubmit(
		@Validated(InsertGroup.class) @ModelAttribute("board") BoardVO board
		, BindingResult errors
		, RedirectAttributes redirectAttributes
	) {
		String lvn;
		if(!errors.hasErrors()) {
			service.createNotice(board);
			lvn = "redirect:/admin/notice/list";
		} else {
			redirectAttributes.addFlashAttribute(MODELNAME, board);
			redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX+MODELNAME, errors);
			lvn = "redirect:/admin/notice/write";
		}
		return lvn;
	}
}
