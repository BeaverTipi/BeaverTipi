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
	
	private static final String RP_FILE_SOURCE_REF = "RPT_BOARD";

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

        // reportDetail이 null이 아니고, 첨부파일이 연결될 Board Number(brdNo)가 있을 경우에만 파일 조회
        if (reportDetail != null && reportDetail.getBrdNo() != null) {
            // 이제 이 쿼리는 'RPT_BOARD'와 'brdNo'를 기준으로 파일을 찾게 됩니다.
            // 파일 저장 시점의 FILE_SOURCE_REF, FILE_SOURCE_ID와 완벽하게 일치하게 됩니다.
            List<FileVO> attachedFiles = fileService.readFileList(RP_FILE_SOURCE_REF, reportDetail.getBrdNo()); 

            reportDetail.setAttachFiles(attachedFiles);
            log.debug("Found reportDetail for reportId: {}, BrdNo: {}, Attached Files Count: {}",
                      reportId, reportDetail.getBrdNo(), attachedFiles != null ? attachedFiles.size() : 0);
        } else {
            log.warn("No report detail found or brdNo is null for reportId: {}. No attempt to fetch files.", reportId);
            // 이 경우 attachFiles는 기본적으로 빈 리스트로 유지되거나, 필요에 따라 초기화할 수 있습니다.
            if (reportDetail != null) {
                reportDetail.setAttachFiles(List.of()); // 빈 리스트로 명시적 설정
            }
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