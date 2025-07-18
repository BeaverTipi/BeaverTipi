package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="adsId")
public class AdsClientVO implements Serializable {
	private String adsId;	// 광고 번호
	private String brdNo;	// 게시글 번호
	private String adsStatusCode;	// 광고 상태 코드
	private String adsBp;	// 사업장명(회사명)
	private String adsPic;	// 담당자명
	private String adsPicTelno;	// 담당자 연락처
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate adsReqPblsStartDt; // 광고 요청 게재 시작날짜 	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate adsReqPblsEndDt;	// 광고 요청 개제 종료날짜
	
}
