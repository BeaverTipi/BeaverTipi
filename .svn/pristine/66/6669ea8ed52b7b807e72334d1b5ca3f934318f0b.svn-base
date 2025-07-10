package kr.or.ddit.admin.mapper;

import java.util.List;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.util.page.PaginationInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportPostMapper {

    List<BoardVO> selectReportedPostList(PaginationInfo<BoardVO> pagingVO);

    int selectReportedPostCount(PaginationInfo<BoardVO> pagingVO);

    int updateReportStatus(BoardVO reportVO);
}