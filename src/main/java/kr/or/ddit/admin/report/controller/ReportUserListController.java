package kr.or.ddit.admin.report.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.inject.Inject;
import kr.or.ddit.admin.report.service.ReportPostService;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.ReportVO;
import kr.or.ddit.vo.ReportSearchVO; // ReportSearchVO 임포트 추가
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
        @ModelAttribute("detailSearch") ReportSearchVO detailSearch, // ReportVO -> ReportSearchVO로 변경
        @ModelAttribute("simpleSearch") SimpleSearch simpleSearch,
        Model model) {

        log.info("detailSearch: {}", detailSearch);
        log.info("simpleSearch: {}", simpleSearch);

        // PaginationInfo의 타입 파라미터는 여전히 ReportVO입니다.
        PaginationInfo<ReportVO> pagingVO = new PaginationInfo<>();
        pagingVO.setCurrentPageNo(currentPage);

        // JSP에 보여줄 원본 brdPblsDtmTo 값을 저장 (ReportSearchVO에서 접근)
        LocalDate originalBrdPblsDtmTo = null;
        if (detailSearch.getBrdPblsDtmTo() != null) {
            originalBrdPblsDtmTo = detailSearch.getBrdPblsDtmTo();
            detailSearch.setBrdPblsDtmTo(detailSearch.getBrdPblsDtmTo().plusDays(1));
        }

        // PaginationInfo에 ReportSearchVO 객체를 detailSearch로 설정
        pagingVO.setDetailSearch(detailSearch);
        pagingVO.setSimpleSearch(simpleSearch);

        int totalCount = reportPostService.selectReportedPostCount(pagingVO);
        pagingVO.setTotalRecordCount(totalCount);

        List<ReportVO> reportedUserList = reportPostService.selectReportedPostList(pagingVO);

        model.addAttribute("reportedUserList", reportedUserList);
        model.addAttribute("pagingVO", pagingVO);

        DefaultPaginationRenderer renderer = new DefaultPaginationRenderer();
        String pagingHTML = renderer.renderPagination(pagingVO, "fn_paging");
        model.addAttribute("pagingHTML", pagingHTML);

        // 백업해둔 원본 값을 detailSearch 객체에 다시 설정하여 JSP로 보냄
        if (originalBrdPblsDtmTo != null) {
            detailSearch.setBrdPblsDtmTo(originalBrdPblsDtmTo);
        }
        model.addAttribute("detailSearch", detailSearch); // ReportSearchVO 타입의 detailSearch 유지

        return "admin/report/userList";
    }

    // 이하는 이전과 동일 (List<ReportVO>, ReportVO, Map 사용)
    @PostMapping("updateStatuses")
    @ResponseBody
    public String updateReportStatuses(@RequestBody List<ReportVO> rptStatusUpdates) {
        try {
            log.info("Received update requests for statuses: {}", rptStatusUpdates);
            int updatedCount = 0;
            for (ReportVO report : rptStatusUpdates) {
                reportPostService.updateReportStatus(report);
                updatedCount++;
            }

            return "{\"status\": \"success\", \"message\": \"" + updatedCount + "건의 신고 상태가 성공적으로 저장되었습니다.\"}";
        } catch (Exception e) {
            log.error("Error updating report statuses: ", e);
            return "{\"status\": \"error\", \"message\": \"신고 상태 저장 중 오류가 발생했습니다.\"}";
        }
    }

    @GetMapping("/detail/{reportId}")
    @ResponseBody
    public ReportVO getReportDetail(@PathVariable String reportId) {
        log.info("신고 상세 조회 요청. reportId: {}" + reportId);
        ReportVO reportDetail = reportPostService.selectReportDetail(reportId);
        if (reportDetail != null) {
            log.info("Report Detail fetched: {}", reportDetail);
        } else {
            log.warn("No report detail found for reportId: {}", reportId);
        }
        return reportDetail;
    }

    @PostMapping("/updateMemberStatus")
    @ResponseBody
    public String updateMemberStatus(@RequestParam String mbrCd, @RequestParam String mbrStatus) {
        log.info("updateMemberStatus called. mbrCd: {}, mbrStatus: {}", mbrCd, mbrStatus);
        try {
            reportPostService.updateReportedMemberStatus(mbrCd, mbrStatus);
            return "SUCCESS";
        } catch (Exception e) {
            log.error("회원 상태 변경 실패", e);
            return "FAIL";
        }
    }

    @PostMapping("/updateListingDeleteStatus")
    @ResponseBody
    public String updateListingDeleteStatus(@RequestParam String lstgId, @RequestParam String lstgDel) {
        log.info("updateListingDeleteStatus called. lstgId: {}, lstgDel: {}", lstgId, lstgDel);
        try {
            reportPostService.updateListingDeleteStatus(lstgId, lstgDel);
            return "SUCCESS";
        } catch (Exception e) {
            log.error("매물 삭제 상태 변경 실패", e);
            return "FAIL";
        }
    }
}