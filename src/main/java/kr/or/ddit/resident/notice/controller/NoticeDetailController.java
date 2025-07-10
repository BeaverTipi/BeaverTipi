package kr.or.ddit.resident.notice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.notice.service.NoticeService;
import kr.or.ddit.vo.NoticeVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class NoticeDetailController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/resident/notice/detail")
    public String detail(
            @RequestParam String noticeNo,
            @RequestParam String bldgIdParam,
            @RequestParam(defaultValue = "") String noticeType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String searchType,
            @RequestParam(defaultValue = "") String searchWord,
            Model model
    ) {
    	NoticeVO voForCount = new NoticeVO();
    	voForCount.setNoticeNo(noticeNo);
    	noticeService.viewCount(voForCount);
    	
    	NoticeVO detail = noticeService.getNoticeById(noticeNo);
    	
    	 model.addAttribute("notice",      detail);
         model.addAttribute("bldgIdParam", bldgIdParam);
         model.addAttribute("noticeType",  noticeType);
         model.addAttribute("page",        page);
         model.addAttribute("searchType",  searchType);
         model.addAttribute("searchWord",  searchWord);

        return "resident/notice/NoticeDetail";
    }
}