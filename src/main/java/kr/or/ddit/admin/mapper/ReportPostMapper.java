//package kr.or.ddit.admin.mapper;
//
//import java.util.List;
//import kr.or.ddit.vo.BoardVO;
//// ⭐ 수정된 부분: kr.or.ddit.vo.PaginationInfoVO 대신 kr.or.ddit.util.page.PaginationInfo를 import ⭐
//import kr.or.ddit.util.page.PaginationInfo;
//import org.apache.ibatis.annotations.Mapper;
//
//@Mapper
//public interface ReportPostMapper {
//
//    // ⭐ 수정된 부분: PaginationInfoVO<BoardVO> 대신 PaginationInfo<BoardVO> 사용 ⭐
//    List<BoardVO> selectReportedPostList(PaginationInfo<BoardVO> pagingVO);
//
//    // ⭐ 수정된 부분: PaginationInfoVO<BoardVO> 대신 PaginationInfo<BoardVO> 사용 ⭐
//    int selectReportedPostCount(PaginationInfo<BoardVO> pagingVO);
//
//    int updateReportStatus(BoardVO reportVO);
//}