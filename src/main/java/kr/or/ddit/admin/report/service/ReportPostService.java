//package kr.or.ddit.admin.report.service;
//



/** 나는 남혁이다. **/
//import java.util.List;
//import kr.or.ddit.vo.BoardVO;
//import kr.or.ddit.util.page.PaginationInfo; // ⭐ import 경로 및 클래스명 변경 ⭐
//import kr.or.ddit.admin.mapper.ReportPostMapper;
//
//public interface ReportPostService {
//
//    // ⭐ 파라미터 타입 변경: PaginationInfoVO -> PaginationInfo ⭐
//    public List<BoardVO> retrieveReportedPostList(PaginationInfo<BoardVO> pagingVO);
//
//    // ⭐ 파라미터 타입 변경: PaginationInfoVO -> PaginationInfo ⭐
//    public int retrieveReportedPostCount(PaginationInfo<BoardVO> pagingVO);
//
//    public int processReport(BoardVO reportVO);
//}