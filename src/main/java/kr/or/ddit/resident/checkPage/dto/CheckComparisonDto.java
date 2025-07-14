package kr.or.ddit.resident.checkPage.dto;

import lombok.Data;

@Data
public class CheckComparisonDto {
	    private String feeCode;          // 항목 코드
	    private String feeName;          // 항목 이름
	    private String description;      // 설명 (선택)
	    private String chargeMonth;      // 청구월

	    private int chargeAmount;        // 당월 요금
	    private int previousAmount;      // 전월 요금
	    private int diffAmount;          // 금액 차이
	    private int percentChange;       // 증감률 (%)

}
