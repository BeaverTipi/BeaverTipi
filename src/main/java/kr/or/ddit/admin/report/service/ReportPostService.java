package kr.or.ddit.admin.report.service;

import java.util.List;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.admin.mapper.ReportPostMapper; 

public interface ReportPostService {

    public List<BoardVO> retrieveReportedPostList(PaginationInfo<BoardVO> pagingVO);

    public int retrieveReportedPostCount(PaginationInfo<BoardVO> pagingVO);

    public int processReport(BoardVO reportVO);
}