package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ReportSearchVO implements Serializable {
    private String searchTitle;       // 제목 검색 (게시글 제목)
    private String searchWriter;      // 작성자 ID (MBR_CD) 검색 (신고자 ID)
    private String searchReportedTargetId; // 피신고자/피신고매물 ID 검색
    private String searchRptStatusCode; // 신고 처리 상태 검색
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate brdPblsDtmFrom; // 게시글 게시일시 (신고일자로 사용) 시작
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate brdPblsDtmTo;   // 게시글 게시일시 (신고일자로 사용) 종료
    private String searchRptCode;     // 신고 유형 검색 (탭 전환 시 사용)
}