package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class BusinessAdsSearchVO implements Serializable {
	private String searchAdsId;			// 광고 번호 (일단 사용안함)
	private String searchAdsBrdNo;		// 게시글 번호 (일단 사용안함)
    private String searchAdsTitle;		// 광고 제목 검색
    private String searchAdsWriter;		// 광고 작성자 검색
    private String searchAdsStatusCode; // 광고 상태 코드 검색
    private String searchAdsBp;         // 사업장명(회사명) 검색
    private String searchAdsPic;        // 담당자명 검색
    private String searchAdsPicTelno;   // 담당자 연락처 검색
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate adsReqPblsStartDt; // 광고 요청 게재 시작날짜 (일단 사용안함)	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate adsReqPblsEndDt;	 // 광고 요청 개제 종료날짜 (일단 사용안함)


}
