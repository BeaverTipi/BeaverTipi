package kr.or.ddit.admin.report.controller;

import java.util.List;

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
// ⭐ 아래 임포트 경로를 수정합니다. ⭐
import kr.or.ddit.util.renderer.DefaultPaginationRenderer; // DefaultPaginationRenderer 임포트 경로 수정
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.vo.BoardVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/report")
public class ReportUserListController {

    @Inject
    private ReportPostService reportPostService;

    @GetMapping("userList")
    public String selectReportedPostList(
        @RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
        @ModelAttribute("detailSearch") BoardVO detailSearch,
        @ModelAttribute("simpleSearch") SimpleSearch simpleSearch,
        Model model
    ) {
        log.info("detailSearch: {}", detailSearch);
        log.info("simpleSearch: {}", simpleSearch);

        PaginationInfo<BoardVO> pagingVO = new PaginationInfo<>();
        pagingVO.setCurrentPageNo(currentPage);

        pagingVO.setDetailSearch(detailSearch);
        pagingVO.setSimpleSearch(simpleSearch);

        List<BoardVO> reportedUserList = reportPostService.selectReportedPostList(pagingVO);
        
        model.addAttribute("reportedUserList", reportedUserList);
        model.addAttribute("pagingVO", pagingVO);
        
        // 페이징 HTML 생성 및 모델에 추가
        DefaultPaginationRenderer renderer = new DefaultPaginationRenderer();
        String pagingHTML = renderer.renderPagination(pagingVO, "fn_paging");
        model.addAttribute("pagingHTML", pagingHTML);

        return "admin/report/userList";
    }

    @PostMapping("updateStatuses") 
    @ResponseBody
    public String updateReportStatuses(@RequestBody List<BoardVO> rptStatusUpdates) {
        try {
            log.info("Received update requests for statuses: {}", rptStatusUpdates);
            int updatedCount = 0;
            for (BoardVO report : rptStatusUpdates) {
                reportPostService.updateReportStatus(report);
                updatedCount++;
            }
            
            return "{\"status\": \"success\", \"message\": \"" + updatedCount + "건의 신고 상태가 성공적으로 저장되었습니다.\"}";
        } catch (Exception e) {
            log.error("Error updating report statuses: ", e);
            return "{\"status\": \"error\", \"message\": \"신고 상태 저장 중 오류가 발생했습니다.\"}";
        }
    }
}