//package kr.or.ddit.admin.report.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import kr.or.ddit.admin.mapper.ReportPostMapper;
//import kr.or.ddit.util.page.PaginationInfo; // ⭐ import 경로 및 클래스명 변경 ⭐
//import kr.or.ddit.vo.BoardVO;
//
//@Service
//public class ReportPostServiceImpl implements ReportPostService {
//
//    @Autowired
//    private ReportPostMapper reportPostMapper;
//
//    @Override
//    public List<BoardVO> retrieveReportedPostList(PaginationInfo<BoardVO> pagingVO) {
//        // ⭐ totalRecord -> totalRecordCount, pagingVO.setTotalRecord -> pagingVO.setTotalRecordCount ⭐
//        pagingVO.setTotalRecordCount(reportPostMapper.selectReportedPostCount(pagingVO));
//        return reportPostMapper.selectReportedPostList(pagingVO);
//    }
//
//    @Override
//    public int retrieveReportedPostCount(PaginationInfo<BoardVO> pagingVO) {
//        return reportPostMapper.selectReportedPostCount(pagingVO);
//    }
//
//    @Override
//    public int processReport(BoardVO reportVO) {
//        return reportPostMapper.updateReportStatus(reportVO);
//    }
//}