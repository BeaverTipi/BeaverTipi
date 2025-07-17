package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StandardLeaseFormDTO implements Serializable {

    // 기본 계약 정보
    private String check;

    // 중개인 정보
    @NotBlank
    private String agentMbrCd;
    private String agentMbrId;
    private String agentName;
    private String agentTelno;
    private String agentEmlAddr;
    private String agentOfficeName;
    private String agentOfficeAddr;
    private String agentRegNo;
    private String agentRep;
    private String agentCrtfNo;
    private String agentTelNo;

    // 임대인
    private Map<String, Object> lessorInfo; // "0": {...}, "1": {...}
    private Map<String, Object> coLessor;   // "1": {...}
    @NotBlank
    private String lessorMbrCd;
    private String lessorMbrId;
    private String lessorName;
    private String lessorTelno;
    private String lessorEmlAddr;
    private String lessorBasicAddr;
    private String lessorDetailAddr;
    private String lessorRegNo1;
    private String lessorRegNo2;
    private String lessorrentalPtyId;
    private String lessorYnTypeCd;
    private String lessorBankNm;
    private String lessorBankAcc;

    // 임차인
    private Map<String, Object> lesseeInfo;
    @NotBlank
    private String lesseeMbrCd;
    private String lesseeMbrId;
    private String lesseeName;
    private String lesseeTelno;
    private String lesseeEmlAddr;
    private String lesseeBasicAddr;
    private String lesseeDetailAddr;
    private String lesseeRegNo1;
    private String lesseeRegNo2;

    // 샘플
    private String contractSampleId;

    // 주택 정보
    @NotBlank
    private String listingId;
    private String listingName;
    private String listingTypeSale;
    private String listingTypeSaleKorean;
    private String listingLocation;
    private String listingAdd;
    private String listingLand;
    private double listingLandArea;
    @NotBlank
    private String listingTypeCode1;
    private String listingExArea;
    private String listingAdd2;
    private String listingGrArea;
    private String listingNewOrAgain;
    private String listingLeaseAmt;
    private String listingLeaseAmtKorean;
    private String listingLease;
    private String listingLeaseKorean;
    private String listingLeaseM;
    private String listingLeaseMKorean;
    private String lessorTaxYN;

    // 계약 날짜 관련
    private String startDate;
    private String startDateYear;
    private String startDateMonth;
    private String startDateDay;

    private String endDate;
    private String endDateYear;
    private String endDateMonth;
    private String endDateDay;

    private String issueDateYear;
    private String issueDateMonth;
    private String issueDateDay;

    private String letInDateYear;
    private String letInDateMonth;
    private String letInDateDay;

    private Date contractConclusionDate;
    private int contractConclusionDateYear;
    private int contractConclusionDateMonth;
    private int contractConclusionDateDay;

    // 계약 금액 관련
    private String listingDeposit;
    private String listingDepositDay;
    private String middlePayment;
    private String balancePayment;

    // 관리비
    private String managementTotal;
    private String management1;
    private String management2;
    private String management3;
    private String management4;
    private String management5;
    private String management6;
    private String management7;
    private String management8;
    private String managementOther;

    // 수리 관련
    private String repairNeed;
    private String repairNeedYN;
    private String repairDeadlineDate;
    private String repairDeadlineDateYear;
    private String repairDeadlineDateMonth;
    private String repairDeadlineDateDay;
    private String repairCostCoveredBy;
    private String lessorBurden;
    private String lesseeBurden;

    // 중개보수
    private String commissionRate;
    private String commissionFee;
    private String commissionTaxIncludedY;
    private String commissionTaxIncludedN;

    // 특약사항
    private String specialTerms;

    // 기타
    private String contractY;
    private String contractN;

    // 2주 후 관련
    private String twoWeeksLaterDate;
    private String twoWeeksLaterDateYear;
    private String twoWeeksLaterDateMonth;
    private String twoWeeksLaterDateDay;

    // 파일
    private List<FileVO> files;

    // 내부적으로 관리되는 상태 (필요시)
    private Map<String, Object> tenancy; // tenancy: { "0": {} }

}
