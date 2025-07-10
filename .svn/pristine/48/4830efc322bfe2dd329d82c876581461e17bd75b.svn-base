package kr.or.ddit.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Mapper
public interface ReportPostMapper {

    List<BoardVO> selectReportedPostList(PaginationInfo<BoardVO> pagingVO);

    int selectReportedPostCount(PaginationInfo<BoardVO> pagingVO);

    int updateReportStatus(BoardVO reportVO);

	BoardVO selectReportDetailByReportId(String reportId);

	void updateMemberStatus(String mbrCd, String mbrStatus);
}