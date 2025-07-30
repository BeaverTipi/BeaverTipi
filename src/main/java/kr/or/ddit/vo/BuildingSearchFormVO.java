package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class BuildingSearchFormVO implements Serializable {
    private String rentalPtyId;     // 임대 아이디
    private String searchBuildingName;  // 건물명
    private String searchRoomNum;       // 호수
    private String searchStatus;         // 상태 코드
    private String searchType;           // 건물 유형 코드
    private String searchRegDateFrom;    // 등록일 검색 시작일
    private String searchRegDateTo;      // 등록일 검색 종료일
    private int page = 1;                // 페이징 현재 페이지
}
