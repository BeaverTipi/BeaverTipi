package kr.or.ddit.main.report.service;

import java.util.List; // List 임포트 추가

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // MultipartFile 임포트

import jakarta.inject.Inject;
import kr.or.ddit.main.mapper.CreateReportMapper;
import kr.or.ddit.util.file.service.FileService; // FileService 주입 (핵심 변경 사항)
import kr.or.ddit.vo.FileVO; // FileVO 임포트 (FileService 반환 타입 등으로 사용될 수 있음)
import kr.or.ddit.vo.ReportVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreateReportServiceImpl implements CreateReportService {

	@Autowired
    private CreateReportMapper createReportMapper;

	@Autowired
    private FileService fileService; // FileService 주입 - 광고 요청 서비스와 동일

    // 신고 관련 파일의 sourceRef
    private static final String REPORT_FILE_SOURCE_REF = "RPT_BOARD"; // 신고 게시글 파일임을 나타내는 참조 코드

    /**
     * 새로운 신고를 생성하는 트랜잭션 메소드입니다.
     * BOARD 테이블과 REPORT 테이블에 각각 데이터를 삽입하고,
     * FileService를 통해 첨부 파일도 서버에 저장하고 DB에 메타데이터를 기록합니다.
     * @param reportVO 신고 게시글 정보 및 신고 상세 정보를 담은 VO
     * @param attachFiles 첨부된 MultipartFile 리스트
     * @return 신고 생성 처리 성공 여부 (true/false)
     */
    @Override
    @Transactional // 여러 DB 작업이 하나의 단위로 처리되도록 트랜잭션 설정
    public boolean createReport(ReportVO reportVO, List<MultipartFile> attachFiles) { // 반환 타입 boolean으로 변경
        try {
            // 1. BOARD 테이블에 게시글 정보 삽입
            // ReportVO가 BoardVO를 상속했으므로, ReportVO 객체 자체가 BoardVO의 필드들을 가지고 있습니다.
            // insertReportBoard 호출 시 selectKey에 의해 brdNo가 reportVO 객체에 자동 생성되어 채워집니다.
            int boardInsertResult = createReportMapper.insertReportBoard(reportVO);
            if (boardInsertResult == 0) {
                log.error("BOARD 테이블 삽입 실패. ReportVO: {}", reportVO);
                // 트랜잭션 롤백 유도를 위해 RuntimeException 던짐
                throw new RuntimeException("신고 게시글 삽입에 실패했습니다.");
            }
            
            String generatedBrdNo = reportVO.getBrdNo(); // 새로 생성된 brdNo 가져오기
            log.debug("새로 생성된 BRD_NO: {}", generatedBrdNo);

            // 2. REPORT 테이블에 신고 상세 정보 삽입
            // ReportVO의 rptId 필드는 CreateReportMapper.xml의 selectKey에 의해 자동 생성되어 채워질 것입니다.
            reportVO.setBrdNo(generatedBrdNo); // BOARD 테이블에서 생성된 brdNo를 ReportVO에 설정
            reportVO.setRptStatusCode("REG"); // 초기 신고 상태는 '등록'으로 설정 (DB 디폴트 값과 일치시키기)

            int reportInsertResult = createReportMapper.insertReport(reportVO);
            if (reportInsertResult == 0) {
                log.error("REPORT 테이블 삽입 실패. ReportVO: {}", reportVO);
                // 트랜잭션 롤백 유도를 위해 RuntimeException 던짐
                throw new RuntimeException("신고 상세 정보 삽입에 실패했습니다.");
            }

            // 3. 파일 첨부 처리 (광고 요청 서비스의 MemberAdsServiceImpl과 동일한 방식 적용)
            if (attachFiles != null && !attachFiles.isEmpty()) {
                log.debug("첨부 파일 {}개 발견. FileService를 통해 파일 처리 시작.", attachFiles.size());
                // FileService의 uploadMultipleFiles 메소드 호출:
                // 실제 파일 저장 (서버), FileVO 생성 및 필수 메타데이터 채우기, DB에 파일 정보 삽입까지 처리
                List<FileVO> uploadedFiles = fileService.uploadMultipleFiles(
                    attachFiles,
                    "public/admin/report", // 신고 관련 파일 저장 경로 (광고 요청 경로와 구분)
                    REPORT_FILE_SOURCE_REF,
                    generatedBrdNo,
                    "RPT_DOC" // 파일 문서 유형 코드 (신고 문서 유형으로 구분)
                );

                // fileService.uploadMultipleFiles 내부에서 파일 처리 실패 시 예외를 던진다고 가정합니다.
                // 따라서 여기서 별도의 uploadedFiles.isEmpty() 검증은 필요 없을 수 있습니다.
                log.debug("FileService를 통한 파일 업로드 및 DB 저장 완료. 처리된 파일 수: {}", uploadedFiles.size());
            } else {
                log.debug("첨부 파일 없음.");
            }

            log.info("createReport 서비스 종료 - 신고 생성 성공. 생성된 신고 게시글 번호: {}", generatedBrdNo);
            return true; // 모든 작업 성공
        } catch (Exception e) {
            log.error("신고 요청 저장 중 오류 발생: {}", e.getMessage(), e);
            // @Transactional에 의해 RuntimeException 또는 다른 체크되지 않은 예외 발생 시 자동 롤백됩니다.
            return false; // 작업 실패
        }
    }
}