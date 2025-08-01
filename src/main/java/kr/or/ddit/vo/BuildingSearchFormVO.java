package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class BuildingSearchFormVO implements Serializable {
    private String rentalPtyId;     // 임대 아이디
    private String searchBuildingName;  // 건물명
    private String searchBuildingRoomNum;       // 호수
    private String searchBuildingStatus;         // 상태 코드
    private String searchBuildingType;           // 건물 유형 코드
    private String searchBuildingRegDateFrom;    // 등록일 검색 시작일
    private String searchBuildingRegDateTo;      // 등록일 검색 종료일
    private int page = 1;                // 페이징 현재 페이지
    
    private Integer activeTab;
}
