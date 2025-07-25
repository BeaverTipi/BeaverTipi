package kr.or.ddit.admin.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import kr.or.ddit.admin.mapper.ReportPostMapper;
import kr.or.ddit.util.file.mapper.FileMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ReportVO;
import kr.or.ddit.vo.ReportSearchVO; // ReportSearchVO 임포트 유지
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportPostServiceImpl implements ReportPostService {

    @Inject
    private ReportPostMapper reportPostMapper;

    @Inject
    private FileMapper fileMapper;

    @Override
    public List<ReportVO> selectReportedPostList(PaginationInfo<ReportVO> pagingVO) {
        return reportPostMapper.selectReportedPostList(pagingVO);
    }

    @Override
    public int selectReportedPostCount(PaginationInfo<ReportVO> pagingVO) {
        return reportPostMapper.selectReportedPostCount(pagingVO);
    }

    @Override
    public int updateReportStatus(ReportVO reportVO) {
        return reportPostMapper.updateReportStatus(reportVO);
    }

    @Override
    public ReportVO selectReportDetail(String reportId) {
        ReportVO reportDetail = reportPostMapper.selectReportDetailByReportId(reportId);
        log.info("Report Detail fetched (ReportVO): {}", reportDetail);

        if (reportDetail != null && reportDetail.getBrdNo() != null) {
            log.debug("Found reportDetail for reportId: {}, brdNo: {}", reportId, reportDetail.getBrdNo());

        } else {
            log.warn("No report detail found or brdNo is null for reportId: {}", reportId);
        }
        return reportDetail;
    }

    @Override
    public void updateReportedMemberStatus(String mbrCd, String mbrStatus) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mbrId", mbrCd);
        paramMap.put("mbrStatus", mbrStatus);
        reportPostMapper.updateMemberStatus(paramMap);
    }

    @Override
    public void updateListingDeleteStatus(String lstgId, String lstgDel) {
        log.info("updateListingDeleteStatus 호출. lstgId: {}, lstgDel: {}", lstgId, lstgDel);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("lstgId", lstgId);
        paramMap.put("lstgDel", lstgDel);
        reportPostMapper.updateListingDeleteStatus(paramMap);
    }
}