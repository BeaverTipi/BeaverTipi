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

            // 첨부 파일 목록을 가져옵니다. (fileSourceId는 게시글 번호)
            FileVO searchFileVO = new FileVO();
            searchFileVO.setFileSourceId(reportDetail.getBrdNo()); // ReportVO의 brdNo 필드 직접 사용
            List<FileVO> attachFiles = fileMapper.selectFileList(searchFileVO);

            // ReportVO에 attachFiles 필드를 직접 추가하거나,
            // BoardVO의 attachFiles 필드를 통해 접근하는 방식을 유지할 수 있습니다.
            // 현재 BoardVO에 attachFiles가 있으므로, ReportVO를 반환할 때 이 필드가 채워지도록 하는 게 자연스럽습니다.
            // 이 로직은 `ReportVO.attachFiles = attachFiles;` 또는 `super.setAttachFiles(attachFiles);` 형태로 가능합니다.
            // BoardVO에 attachFiles 필드가 있다면, super.setAttachFiles(attachFiles)로 가능합니다.
            reportDetail.setAttachFiles(attachFiles); // ReportVO가 BoardVO를 상속했으므로, BoardVO의 setAttachFiles 메서드 호출
            log.debug("Attached files count for brdNo {}: {}", reportDetail.getBrdNo(), attachFiles.size());

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