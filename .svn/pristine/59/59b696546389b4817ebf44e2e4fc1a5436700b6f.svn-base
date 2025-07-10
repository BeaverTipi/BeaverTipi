package kr.or.ddit.admin.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import kr.or.ddit.admin.mapper.ReportPostMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Service
public class ReportPostServiceImpl implements ReportPostService {

    @Inject
    private ReportPostMapper reportPostMapper;

    @Override
    public List<BoardVO> selectReportedPostList(PaginationInfo<BoardVO> pagingVO) {
    	int totalRecord = reportPostMapper.selectReportedPostCount(pagingVO);
        pagingVO.setTotalRecordCount(reportPostMapper.selectReportedPostCount(pagingVO));
        return reportPostMapper.selectReportedPostList(pagingVO);
    }

    @Override
    public int selectReportedPostCount(PaginationInfo<BoardVO> pagingVO) {
        return reportPostMapper.selectReportedPostCount(pagingVO);
    }

    @Override
    public int processReport(BoardVO reportVO) {
        return reportPostMapper.updateReportStatus(reportVO);
    }

	@Override
	public int updateReportStatus(BoardVO boardVO) {
		return reportPostMapper.updateReportStatus(boardVO);
	}
}