package kr.or.ddit.admin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.ReportVO;

@Mapper
public interface ReportPostMapper {

    List<ReportVO> selectReportedPostList(PaginationInfo<ReportVO> pagingVO);

    int selectReportedPostCount(PaginationInfo<ReportVO> pagingVO);

    int updateReportStatus(ReportVO reportVO);

    ReportVO selectReportDetailByReportId(String reportId);

	void updateMemberStatus(Map<String, String> paramMap);

	void updateListingDeleteStatus(Map<String, String> paramMap);
}