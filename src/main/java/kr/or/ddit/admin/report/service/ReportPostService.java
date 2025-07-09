package kr.or.ddit.admin.report.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO; 

public interface ReportPostService {

    public List<BoardVO> selectReportedPostList(PaginationInfo<BoardVO> pagingVO);

    public int updateReportStatus(BoardVO boardVO);
    
    public int selectReportedPostCount(PaginationInfo<BoardVO> pagingVO);

    public int processReport(BoardVO reportVO);
}