package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="chgbillId")
public class ChargeBillVO implements Serializable {
    private String chgbillId;
    private String rentalPtyId;
    private String unitId;
    private String bldgId;
    private String chgbillChargeMonth;
    private long chgbillAmount;
    private String chgbillStatus;
    private String chgbillStatusGrpCd;
    private String chgbillDueDate;  // 날짜는 String으로 저장
    private String chgbillPaidDate;  // 날짜는 String으로 저장
    private String chgbillDesc;
    
    private String intgFeeId;
    private long intgFeeAmount;
    private String chargeMonth;
    private String feeStatus;

    private Double energyUsageQty;
    private Long energyChargeAmount;
    private String energyType;

    private String residentName;
    private String buildingName;


    // String으로 저장된 날짜를 LocalDate로 변환
    public LocalDate getChgbillDueDateAsLocalDate() {
        return chgbillDueDate != null && !chgbillDueDate.isEmpty() 
               ? LocalDate.parse(chgbillDueDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
               : null;
    }

    public LocalDate getChgbillPaidDateAsLocalDate() {
        return chgbillPaidDate != null && !chgbillPaidDate.isEmpty() 
               ? LocalDate.parse(chgbillPaidDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
               : null;
    }

    // 날짜를 String으로 변환
    public String getChgbillDueDateAsString() {
        return chgbillDueDate != null ? chgbillDueDate : null;
    }

    public String getChgbillPaidDateAsString() {
        return chgbillPaidDate != null ? chgbillPaidDate : null;
    }

    // 날짜 파싱 메서드 (문자열을 LocalDate로 변환하고 yyyyMMdd 형식으로 반환)
    public void setChgbillDueDateFromString(String dueDate) {
        if (dueDate != null && !dueDate.isEmpty()) {
            this.chgbillDueDate = LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }

    public void setChgbillPaidDateFromString(String paidDate) {
        if (paidDate != null && !paidDate.isEmpty()) {
            this.chgbillPaidDate = LocalDate.parse(paidDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }
    public String getFormattedDueDate() {
        if (chgbillDueDate == null || chgbillDueDate.length() != 8) return "";
        return chgbillDueDate.substring(0, 4) + "-" +
               chgbillDueDate.substring(4, 6) + "-" +
               chgbillDueDate.substring(6, 8);
    }
}
