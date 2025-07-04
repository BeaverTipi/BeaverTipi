//package kr.or.ddit.admin.board.controller;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.inject.Inject;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import kr.or.ddit.admin.report.service.ReportPostService;
//import kr.or.ddit.util.page.PaginationInfo; // ⭐ import 경로 및 클래스명 변경 ⭐
//import kr.or.ddit.vo.BoardVO;
//
//@Controller
//@RequestMapping("/admin/report")
//public class ReportUserListController {
//
//    @Inject
//    private ReportPostService reportPostService;
//
//    @GetMapping("/userList")
//    public String reportUserList(
//            // ⭐ currentPage -> currentPageNo 로 변경 ⭐
//            @RequestParam(name = "page", required = false, defaultValue = "1") int currentPageNo,
//            // ⭐ searchCondition -> detailSearch 로 변경 ⭐
//            @ModelAttribute("detailSearch") BoardVO detailSearch,
//            Model model
//    ) {
//        // ⭐ PaginationInfoVO -> PaginationInfo 로 변경 ⭐
//        PaginationInfo<BoardVO> pagingVO = new PaginationInfo<BoardVO>();
//        
//        // ⭐ searchCondition -> detailSearch 로 변경 ⭐
//        pagingVO.setDetailSearch(detailSearch); 
//        // ⭐ currentPage -> currentPageNo 로 변경 ⭐
//        pagingVO.setCurrentPageNo(currentPageNo);
//
//        // ⭐ totalRecord -> totalRecordCount, retrieveReportedPostCount 호출 후 setTotalRecordCount 호출 ⭐
//        // totalRecordCount는 getter/setter가 없으므로 계산을 setTotalRecordCount 내부에서 하도록 유도
//        // (원래 PaginationInfo 클래스의 setTotalRecordCount가 내부적으로 계산함)
//        reportPostService.retrieveReportedPostList(pagingVO); // 이 호출 안에서 totalRecordCount가 설정되고 페이징 계산이 완료됨
//
//        List<BoardVO> reportedUserList = pagingVO.getDataList(); // PaginationInfo에 데이터 리스트를 담는 필드가 없으므로, 직접 리턴받아야 함
//                                                                // 다만, service에서 목록을 리턴하므로 이 부분은 그대로 사용 가능
//
//        model.addAttribute("reportedUserList", reportedUserList);
//        model.addAttribute("pagingVO", pagingVO);
//        // ⭐ searchCondition -> detailSearch 로 변경 ⭐
//        model.addAttribute("detailSearch", detailSearch); 
//
//        return "admin/report/userList"; 
//    }
//
//    @PostMapping("/updateStatus")
//    @ResponseBody
//    public Map<String, String> updateReportStatus(@RequestBody BoardVO reportVO) {
//        Map<String, String> resultMap = new HashMap<>();
//        try {
//            int res = reportPostService.processReport(reportVO);
//            if (res > 0) {
//                resultMap.put("status", "success");
//                resultMap.put("message", "신고 상태가 성공적으로 변경되었습니다.");
//            } else {
//                resultMap.put("status", "fail");
//                resultMap.put("message", "신고 상태 변경에 실패했습니다.");
//            }
//        } catch (Exception e) {
//            resultMap.put("status", "error");
//            resultMap.put("message", "서버 오류: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return resultMap;
//    }
//}