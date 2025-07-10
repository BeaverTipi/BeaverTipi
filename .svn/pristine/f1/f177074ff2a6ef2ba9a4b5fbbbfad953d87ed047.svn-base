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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportPostServiceImpl implements ReportPostService {

    @Inject
    private ReportPostMapper reportPostMapper;

    @Inject
    private FileMapper fileMapper;
    
    @Override
    public List<BoardVO> selectReportedPostList(PaginationInfo<BoardVO> pagingVO) {
    	int totalRecord = reportPostMapper.selectReportedPostCount(pagingVO);
        pagingVO.setTotalRecordCount(totalRecord);
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
	
	@Override
    public BoardVO selectReportDetail(String reportId) {
        // 1. reportId로 신고된 게시글의 상세 정보(BoardVO)를 가져옵니다.
        //    이때 ReportPostMapper.xml에서 BRD_NO (게시글 번호)와 신고 대상 회원의 상태(rptTargetMbrStatus)를 함께 가져오도록 해야 합니다.
        BoardVO reportDetail = reportPostMapper.selectReportDetailByReportId(reportId);
        	log.info("Report Detail fetched: {}", reportDetail);
        if (reportDetail != null && reportDetail.getBrdNo() != null) { // ⭐ 게시글 번호(brdNo)가 있어야 파일을 조회할 수 있습니다. ⭐
            log.debug("Found reportDetail for reportId: {}, brdNo: {}", reportId, reportDetail.getBrdNo());

            // 2. BoardVO의 brdNo를 사용하여 첨부 파일 목록을 가져옵니다.
            //    FileVO의 fileSourceId가 BoardVO의 brdNo와 매핑된다고 가정합니다.
            FileVO searchFileVO = new FileVO();
            searchFileVO.setFileSourceId(reportDetail.getBrdNo()); // 게시글 번호를 파일 소스 ID로 설정

            // 참고: 만약 게시글 첨부 파일에 특정 fileSourceRef를 사용한다면 여기에 설정해야 합니다.
            // 예: searchFileVO.setFileSourceRef("BOARD");

            List<FileVO> attachFiles = fileMapper.selectFileList(searchFileVO);
            reportDetail.setAttachFiles(attachFiles);
            log.debug("Attached files count for brdNo {}: {}", reportDetail.getBrdNo(), attachFiles.size());

        } else {
            log.warn("No report detail found or brdNo is null for reportId: {}", reportId);
        }
        return reportDetail;
    }

    // 신고된 회원의 상태를 변경
    @Override
    public void updateReportedMemberStatus(String mbrCd, String mbrStatus) {
    	Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mbrCd", mbrCd);
        paramMap.put("mbrStatus", mbrStatus);
        reportPostMapper.updateMemberStatus(paramMap); // Map 객체 전달
    }
    
    // 신고된 매물의 상태(삭제여부)를 변경
    @Override
    public void updateListingDeleteStatus(String lstgId, String lstgDel) {
        log.info("updateListingDeleteStatus 호출. lstgId: {}, lstgDel: {}", lstgId, lstgDel);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("lstgId", lstgId);
        paramMap.put("lstgDel", lstgDel);
        reportPostMapper.updateListingDeleteStatus(paramMap);
    }
}