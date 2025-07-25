package kr.or.ddit.main.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.BoardVO; // BoardVO 임포트 (insertReportBoard용)
import kr.or.ddit.vo.ReportVO; // ReportVO 임포트 (insertReport용)

@Mapper
public interface CreateReportMapper {

    /**
     * BOARD 테이블에 새로운 신고 게시글 정보를 삽입합니다.
     * ReportVO가 BoardVO를 상속하므로 ReportVO를 파라미터로 받습니다.
     * ReportVO 객체에 게시글 번호(brdNo)가 selectKey를 통해 자동 생성되어 반환됩니다.
     * @param reportVO 게시글 제목, 내용, 작성자 정보 등을 담은 ReportVO (BoardVO 필드 포함)
     * @return 삽입된 레코드 수 (1이면 성공)
     */
    public int insertReportBoard(ReportVO reportVO); // ReportVO를 받도록 변경

    /**
     * REPORT 테이블에 새로운 신고 상세 정보를 삽입합니다.
     * ReportVO 객체에 신고 번호(rptId)가 selectKey를 통해 자동 생성되어 반환됩니다.
     * @param reportVO 신고 대상 ID, 신고 유형, 게시글 번호(brdNo) 등을 담은 ReportVO
     * @return 삽입된 레코드 수 (1이면 성공)
     */
    public int insertReport(ReportVO reportVO);
}
