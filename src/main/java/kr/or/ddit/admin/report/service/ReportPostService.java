package kr.or.ddit.admin.report.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.ReportVO; 

public interface ReportPostService {

    public List<ReportVO> selectReportedPostList(PaginationInfo<ReportVO> pagingVO);

    public int selectReportedPostCount(PaginationInfo<ReportVO> pagingVO);
    
    public int updateReportStatus(ReportVO reportVO);
    
    public ReportVO selectReportDetail(String reportId);	// 신고 상세 정보를 조회
    
    public void updateReportedMemberStatus(String mbrCd, String mbrStatus);	// 신고된 회원 상태 변경
    
    public void updateListingDeleteStatus(String lstgId, String lstgDel);	// 신고된 매물 상태 변경
}