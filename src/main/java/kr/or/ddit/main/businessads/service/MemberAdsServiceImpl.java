package kr.or.ddit.main.businessads.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 처리를 위해 임포트
import org.springframework.web.multipart.MultipartFile;

import jakarta.inject.Inject;
import kr.or.ddit.main.mapper.MemberAdsMapper; // 새로운 매퍼 인터페이스 주입 예정
import kr.or.ddit.util.file.service.FileService; // 파일 서비스 주입
import kr.or.ddit.vo.AdsClientVO;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.FileVO; // FileVO 임포트
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberAdsServiceImpl implements MemberAdsService {

	@Autowired
    private MemberAdsMapper memberAdsMapper; // MemberAdsMapper 주입

	@Autowired
    private FileService fileService; // FileService 주입

    // 광고 파일의 sourceRef 값 (FileVO의 fileSourceRef 필드에 저장될 값)
    private static final String AD_FILE_SOURCE_REF = "AD_BOARD"; // BusinessAdsServiceImpl과 동일하게 유지

    @Override
    @Transactional // 여러 DB 작업이 하나의 트랜잭션으로 묶이도록 설정 (게시글, 광고주 정보, 파일 저장)
    public boolean createAdsRequest(BoardVO boardVO, List<MultipartFile> attachFiles) {
        try {
            // 1. BoardVO 저장
            // 게시글 번호(brdNo)는 DB에서 시퀀스 등으로 자동 생성되거나, 삽입 전에 미리 할당되어야 합니다.
            // 여기서는 매퍼에서 brdNo를 생성하고 반환받는다고 가정합니다.
            // boardVO.setBrdCode("AD001"); // 광고 게시판 코드 (필요하다면 설정)
            // boardVO.setBrdDelYn("N");   // 삭제 여부 기본값 N
            // boardVO.setBrdVwCnt(0);    // 조회수 0으로 초기화
            // boardVO.setBrdPblsDtm(LocalDateTime.now()); // 현재 시간으로 설정 (또는 DB에서 now())
        	
        	AdsClientVO adsClientVO = boardVO.getAdsClientVO();
        	
            int boardResult = memberAdsMapper.insertBoard(boardVO);
            if (boardResult <= 0) {
                log.error("광고 게시글 (BoardVO) 저장 실패: {}", boardVO);
                return false;
            }
            // 삽입 후 생성된 brdNo를 BoardVO 객체에서 가져와 AdsClientVO와 FileVO에 연결합니다.
            String generatedBrdNo = boardVO.getBrdNo(); // 매퍼에서 <selectKey> 등으로 brdNo를 리턴받는다고 가정

            // 2. AdsClientVO 저장
            // boardVO에서 생성된 brdNo를 AdsClientVO에 설정
            adsClientVO.setBrdNo(generatedBrdNo);
            // adsClientVO.setAdsStatusCode("대기"); // 초기 광고 상태 (대기, 승인, 반려 등)
                                                    // 비즈니스 로직에 따라 기본값 설정
            int adsClientResult = memberAdsMapper.insertAdsClient(adsClientVO);
            if (adsClientResult <= 0) {
                log.error("광고주 정보 (AdsClientVO) 저장 실패: {}", adsClientVO);
                throw new RuntimeException("광고주 정보 저장 실패"); // 트랜잭션 롤백 유도
            }

            // 3. 파일 저장 (첨부 파일이 있을 경우)
            if (attachFiles != null && !attachFiles.isEmpty()) {
                // FileService를 사용하여 파일 업로드 및 DB 저장
                // fileSourceRef는 "AD_BOARD", fileSourceId는 새로 생성된 brdNo
                List<FileVO> uploadedFiles = fileService.uploadMultipleFiles(
                    attachFiles,
                    "public/admin/ads", // S3 버킷 내의 경로 또는 파일 저장 디렉토리
                    AD_FILE_SOURCE_REF,
                    generatedBrdNo,
                    "AD_DOC" // 파일 문서 유형 코드 (필요하다면)
                );
                // 파일 업로드 및 DB 저장이 실패했을 경우 예외 발생 (FileService 내부에서 처리)
                // 또는 여기서 uploadedFiles.isEmpty() 등으로 추가 검증
            }

            log.info("광고 요청 성공적으로 저장 완료. 게시글 번호: {}", generatedBrdNo);
            return true; // 모든 작업 성공
        } catch (Exception e) {
            log.error("광고 요청 저장 중 오류 발생: {}", e.getMessage(), e);
            // @Transactional에 의해 예외 발생 시 자동 롤백됩니다.
            return false; // 작업 실패
        }
    }
}