package kr.or.ddit.admin.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import kr.or.ddit.admin.mapper.ReportPostMapper;
import kr.or.ddit.util.file.mapper.FileMapper;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ReportVO;
import kr.or.ddit.vo.ReportSearchVO; // ReportSearchVO 임포트 유지
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportPostServiceImpl implements ReportPostService {

	@Autowired
    private ReportPostMapper reportPostMapper;

	@Autowired
    private FileService fileService;
	
	private static final String RP_FILE_SOURCE_REF = "RP_BOARD";

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

        if (reportDetail != null && reportDetail.getBrdNo() != null) {
            // **디버깅 필수: reportDetail.getBrdNo() 값이 예상대로 나오는지 확인**
            // **디버깅 필수: fileService.readFileList 호출 후 attachedFiles에 데이터가 있는지 확인**
            List<FileVO> attachedFiles = fileService.readFileList(RP_FILE_SOURCE_REF, reportDetail.getBrdNo());
            reportDetail.setAttachFiles(attachedFiles);
            log.debug("Found reportDetail for reportId: {}, brdNo: {}, Attached Files Count: {}",
                      reportId, reportDetail.getBrdNo(), attachedFiles != null ? attachedFiles.size() : 0);
        } else {
            log.warn("No report detail found or brdNo is null for reportId: {}. No attempt to fetch files.", reportId);
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