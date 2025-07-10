package kr.or.ddit.admin.report.controller;

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
import kr.or.ddit.util.page.SimpleSearch; // SimpleSearch 임포트가 필요한 경우 유지
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
        @ModelAttribute("simpleSearch") SimpleSearch simpleSearch, // SimpleSearch를 사용하지 않는다면 제거
        Model model) {

        log.info("detailSearch: {}", detailSearch); // ⭐ searchRptCode 값이 여기에 찍히는지 확인 ⭐
        log.info("simpleSearch: {}", simpleSearch); // SimpleSearch 사용하지 않는다면 이 로그 제거

        PaginationInfo<BoardVO> pagingVO = new PaginationInfo<>();
        pagingVO.setCurrentPageNo(currentPage);

        pagingVO.setDetailSearch(detailSearch);
        pagingVO.setSimpleSearch(simpleSearch); // SimpleSearch 사용하지 않는다면 이 라인 제거

        List<BoardVO> reportedUserList = reportPostService.selectReportedPostList(pagingVO);

        // ⭐ 페이징을 위한 전체 레코드 수 조회 및 설정 ⭐
        int totalCount = reportPostService.selectReportedPostCount(pagingVO);
        pagingVO.setTotalRecordCount(totalCount);

        model.addAttribute("reportedUserList", reportedUserList);
        model.addAttribute("pagingVO", pagingVO);

        // 페이징 HTML 생성 및 모델에 추가
        DefaultPaginationRenderer renderer = new DefaultPaginationRenderer();
        String pagingHTML = renderer.renderPagination(pagingVO, "fn_paging");
        model.addAttribute("pagingHTML", pagingHTML);

        // ⭐ detailSearch 객체를 다시 Model에 추가하여 JSP 폼에 값 유지 ⭐
        // form:form modelAttribute="detailSearch" 를 사용하므로 이 부분이 중요합니다.
        model.addAttribute("detailSearch", detailSearch);

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

    // 신고 상세 정보 조회
    @GetMapping("/detail/{reportId}")
    @ResponseBody // JSON 응답을 위해 추가
    public BoardVO getReportDetail(@PathVariable String reportId) {
        log.info("getReportDetail called with reportId: " + reportId);
        BoardVO reportDetail = reportPostService.selectReportDetail(reportId);
        if (reportDetail != null) {
            log.info("Report Detail fetched: {}", reportDetail);
            if (reportDetail.getAttachFiles() != null) {
                log.info("Attached Files Count: {}", reportDetail.getAttachFiles().size());
                // 각 파일의 URL을 로그에 출력하여 디버깅에 도움을 줍니다.
                reportDetail.getAttachFiles().forEach(file -> log.info("  File: {} (URL: {})", file.getFileOriginalname(), file.getFilePathUrl()));
            }
        } else {
            log.warn("No report detail found for reportId: {}", reportId);
        }
        return reportDetail;
    }

    // 신고된 회원 상태 변경
    @PostMapping("/updateMemberStatus")
    @ResponseBody // JSON 응답을 위해 추가
    public String updateMemberStatus(@RequestParam String mbrCd, @RequestParam String mbrStatus) {
        log.info("updateMemberStatus called. mbrCd: {}, mbrStatus: {}", mbrCd, mbrStatus);
        try {
            reportPostService.updateReportedMemberStatus(mbrCd, mbrStatus);
            return "SUCCESS"; // 성공 시 "SUCCESS" 문자열 반환
        } catch (Exception e) {
            log.error("회원 상태 변경 실패", e);
            return "FAIL"; // 실패 시 "FAIL" 문자열 반환
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