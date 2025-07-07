package kr.or.ddit.admin.report.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.inject.Inject;
import kr.or.ddit.admin.report.service.ReportPostService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.vo.BoardVO; 

@Controller
@RequestMapping("/admin")
public class ReportUserListController {

    @Inject
    private ReportPostService reportPostService;

    // DefaultPaginationRenderer를 주입
    @Inject
    private DefaultPaginationRenderer renderer; 

    @GetMapping("/report/userList")
    public String reportUserList(
            @RequestParam(name = "page", required = false, defaultValue = "1") int currentPageNo,
            @ModelAttribute("detailSearch") BoardVO detailSearch,
            @ModelAttribute("simpleSearch") SimpleSearch simpleSearch,
            Model model
    ) {
        PaginationInfo<BoardVO> pagingVO = new PaginationInfo<BoardVO>();
        
        pagingVO.setDetailSearch(detailSearch); 
        pagingVO.setSimpleSearch(simpleSearch);
        
        pagingVO.setCurrentPageNo(currentPageNo);

        List<BoardVO> reportedUserList = reportPostService.retrieveReportedPostList(pagingVO);

        model.addAttribute("reportedUserList", reportedUserList);
        model.addAttribute("pagingVO", pagingVO);
        model.addAttribute("detailSearch", detailSearch); 
        model.addAttribute("simpleSearch", simpleSearch);

        // 페이징 HTML 렌더링 및 Model에 추가
        String pagingHTML = renderer.renderPagination(pagingVO, "fn_paging");
        model.addAttribute("pagingHTML", pagingHTML);


        return "admin/report/userList"; 
    }

    @PostMapping("/updateStatus")
    @ResponseBody
    public Map<String, String> updateReportStatus(@RequestBody BoardVO reportVO) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            int res = reportPostService.processReport(reportVO);
            if (res > 0) {
                resultMap.put("status", "success");
                resultMap.put("message", "신고 상태가 성공적으로 변경되었습니다.");
            } else {
                resultMap.put("status", "fail");
                resultMap.put("message", "신고 상태 변경에 실패했습니다.");
            }
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("message", "서버 오류: " + e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }
}