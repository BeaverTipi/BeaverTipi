package kr.or.ddit.main.report.service;

import java.util.List; // List 임포트 추가

import org.springframework.web.multipart.MultipartFile; // MultipartFile 임포트

import kr.or.ddit.vo.ReportVO;

public interface CreateReportService {

    /**
     * 새로운 신고를 생성합니다.
     * ReportVO는 BoardVO를 상속하므로, 게시글 정보와 신고 상세 정보를 모두 포함합니다.
     * 이 메소드는 BOARD 테이블과 REPORT 테이블에 각각 데이터를 삽입하고,
     * FileService를 통해 첨부 파일도 서버에 저장하고 DB에 메타데이터를 기록합니다.
     * @param reportVO 신고 게시글 정보 및 신고 상세 정보를 담은 VO
     * @param attachFiles 첨부된 MultipartFile 리스트 (광고 요청 서비스와 동일하게 List 사용)
     * @return 신고 생성 처리 성공 여부 (true/false)
     */
    public boolean createReport(ReportVO reportVO, List<MultipartFile> attachFiles); // 반환 타입 boolean으로 변경
}