package kr.or.ddit.admin.report.controller;

import java.util.HashMap; // Map 사용을 위해 추가
import java.util.List;
import java.util.Map;     // Map 사용을 위해 추가

import org.springframework.http.ResponseEntity; // ResponseEntity 사용을 위해 추가 (updateStatuses에서 반환 타입 변경 예정)
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable; // 팝업창 변경으로 더 이상 필요 없음
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/report")
public class ReportUserListController {

    @Inject
    private ReportPostService reportPostService;

    // 회원 & 매물 신고 관리 목록 조회 (userList.jsp)
    @GetMapping("userList")
    public String selectReportedPostList(
        @RequestParam(name="page", required = false, defaultValue = "1") int currentPage,
        @ModelAttribute("detailSearch") BoardVO detailSearch,
        @ModelAttribute("simpleSearch") SimpleSearch simpleSearch,
        Model model) {

        log.info("selectReportedPostList() 실행!");
        log.info("detailSearch: {}", detailSearch);
        log.info("simpleSearch: {}", simpleSearch);

        PaginationInfo<BoardVO> pagingVO = new PaginationInfo<>();
        pagingVO.setCurrentPageNo(currentPage);

        pagingVO.setDetailSearch(detailSearch);
        pagingVO.setSimpleSearch(simpleSearch);

        List<BoardVO> reportedUserList = reportPostService.selectReportedPostList(pagingVO);

        int totalCount = reportPostService.selectReportedPostCount(pagingVO);
        pagingVO.setTotalRecordCount(totalCount);
        // ⭐ 중요: PaginationInfo에 dataList 필드와 setter가 있어야 합니다.
        // 만약 없다면, PaginationInfo.java 파일에 private List<T> dataList; 와 @Setter를 추가해야 합니다.
        pagingVO.setDataList(reportedUserList); // 리스트 데이터를 PaginationInfo에 설정

        model.addAttribute("reportedUserList", reportedUserList);
        model.addAttribute("pagingVO", pagingVO);

        DefaultPaginationRenderer renderer = new DefaultPaginationRenderer();
        String pagingHTML = renderer.renderPagination(pagingVO, "fn_paging");
        model.addAttribute("pagingHTML", pagingHTML);

        model.addAttribute("detailSearch", detailSearch);

        return "admin/report/userList";
    }

    // 신고 상태 일괄 업데이트 (JSON 응답이므로 기존과 동일하게 유지)
    @PostMapping("updateStatuses")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateReportStatuses(@RequestBody List<BoardVO> rptStatusUpdates) {
        Map<String, String> response = new HashMap<>();
        try {
            log.info("Received update requests for statuses: {}", rptStatusUpdates);
            int updatedCount = 0;
            for (BoardVO report : rptStatusUpdates) {
                reportPostService.updateReportStatus(report);
                updatedCount++;
            }
            response.put("status", "success");
            response.put("message", updatedCount + "건의 신고 상태가 성공적으로 저장되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating report statuses: ", e);
            response.put("status", "error");
            response.put("message", "신고 상태 저장 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    // ⭐ 변경: 신고 상세 정보 팝업창 호출 (GET 방식) ⭐
    // URL: /admin/report/detailPopup?reportId=xxxx
    @GetMapping("/detailPopup") // 매핑 경로 변경
    public String showReportDetailPopup(@RequestParam("reportId") String reportId, Model model) {
        log.info("showReportDetailPopup called with reportId: " + reportId);

        BoardVO reportDetail = reportPostService.selectReportDetail(reportId);
        model.addAttribute("reportDetail", reportDetail);

        if (reportDetail != null) {
            log.info("Report Detail fetched: {}", reportDetail);
            if (reportDetail.getAttachFiles() != null) {
                log.info("Attached Files Count: {}", reportDetail.getAttachFiles().size());
                reportDetail.getAttachFiles().forEach(file -> log.info("  File: {} (URL: {})", file.getFileOriginalname(), file.getFilePathUrl()));
            }

            // 매물/회원 상태 표시를 위한 추가 속성
            boolean isListingReport = "LSTG".equals(reportDetail.getRptCode());
            model.addAttribute("isListingReport", isListingReport);

            if (isListingReport && reportDetail.getLstgDel() != null) {
                model.addAttribute("currentLstgDelText", reportDetail.getLstgDel().equals("Y") ? "삭제됨" : "활성");
            }

            // ReportUserListController 내 BoardVO는 rptTargetMbrStatus 필드가 없을 수 있습니다.
            // 만약 회원 신고 시 회원의 현재 상태를 표시하려면 BoardVO에 rptTargetMbrStatus 필드가 있어야 합니다.
            // 없으면 MemberVO나 별도의 VO를 통해 가져와야 합니다.
            // 현재 BoardVO에 이 필드가 있다는 가정하에 코드를 작성합니다.
            if (!isListingReport && reportDetail.getRptTargetMbrStatus() != null) {
                 model.addAttribute("currentMbrStatusText", reportDetail.getRptTargetMbrStatus());
            }

        } else {
            log.warn("No report detail found for reportId: {}", reportId);
        }

        // 팝업창으로 사용할 JSP 파일 경로 (예: ManageReport.jsp)
        // 이 JSP는 전체 HTML 구조를 가져야 합니다 (head, body 등)
        return "admin/report/ManageReport";
    }

    // 신고된 회원 상태 변경
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

    // 신고된 매물 삭제 상태 변경
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