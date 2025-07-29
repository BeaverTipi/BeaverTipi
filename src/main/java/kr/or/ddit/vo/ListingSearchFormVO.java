package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ListingSearchFormVO implements Serializable {
    private String mbrCd;                 // 로그인된 사용자 (내 매물 조회용)
    private String searchBuildingName;   // 건물명
    private String searchRoomNum;        // 호수
    private String searchStatus;         // 상태코드 (1:활성, 2:비활성, 3:숨김)
    private String searchType;           // 거래유형 (1:전세, 2:월세, 3:매매)
    private int page = 1;                // 현재 페이지 (기본값 1)

    // 💰 보증금 검색 범위 (만원 단위)
    private Integer searchDepositMin;
    private Integer searchDepositMax;
    
    private Integer searchLeaseMin;
    private Integer searchLeaseMax;
    // 💵 월세 검색 범위 (만원 단위)
    private Integer searchMonthlyMin;
    private Integer searchMonthlyMax;

    // 🏠 매매가 검색 범위 (만원 단위)
    private Integer searchSaleMin;
    private Integer searchSaleMax;
    
    private LocalDate searchRegDateFrom;
    private LocalDate searchRegDateTo;

}
