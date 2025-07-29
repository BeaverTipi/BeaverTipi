package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class ChargeBillVO implements Serializable {
    // 청구 관련 정보
    private String rentalPtyId;
    private String unitId;
    private String bldgId;
    private String chgbillChargeMonth;
    private int chgbillAmount;
    private String chgbillStatus;
    private String chgbillStatusGrpCd;
    private String chgbillDueDate;   // yyyyMMdd
    private String chgbillPaidDate;  // yyyyMMdd
    private String chgbillDesc;
    private String chgbillAccNum;
    private int chgbillPayAmount;
    
    // 코드명 정보
    private String chgbillStatusName;
    private String intManFeeName;
    private String intManFeeDesc;

    // 관계 정보
    private String rentalPartyName;
    private String residentName;
    private String buildingName;

    // 통합관리비 정보
    private String intgFeeId;
    private Long intgFeeAmount;
    private String chargeMonth;
    private String feeStatus;

    // 에너지 사용 정보
    private Double energyUsageQty;
    private Long energyChargeAmount;
    private String energyType;

    // 날짜 변환 유틸
    public LocalDate getChgbillDueDateAsLocalDate() {
        return (chgbillDueDate != null && chgbillDueDate.length() == 8)
            ? LocalDate.parse(chgbillDueDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
            : null;
    }

    public LocalDate getChgbillPaidDateAsLocalDate() {
        return (chgbillPaidDate != null && chgbillPaidDate.length() == 8)
            ? LocalDate.parse(chgbillPaidDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
            : null;
    }

    public void setChgbillDueDateFromString(String dueDate) {
        if (dueDate != null && !dueDate.isEmpty()) {
            this.chgbillDueDate = LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                            .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }

    public void setChgbillPaidDateFromString(String paidDate) {
        if (paidDate != null && !paidDate.isEmpty()) {
            this.chgbillPaidDate = LocalDate.parse(paidDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                             .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }

    public String getFormattedDueDate() {
        return (chgbillDueDate != null && chgbillDueDate.length() == 8)
            ? chgbillDueDate.substring(0, 4) + "-" +
              chgbillDueDate.substring(4, 6) + "-" +
              chgbillDueDate.substring(6, 8)
            : "";
    }
}
