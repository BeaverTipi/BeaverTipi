package kr.or.ddit.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ChargeBillPaymentLogVO implements Serializable {

    private String chgbillChargeMonth;       // 납부월
    private String rentalPtyId;              // 임대인 ID
    private String unitId;                   // 호 ID
    private String bldgId;                   // 건물 ID

    private String paymentKey;               // 결제키
    private Long chgbillPayAmount;           // 납부 금액
    private Date chgbillPaidAt;              // 납부 일시

    private String chgbillPayMethod;         // 납부 수단
    private String chgbillPayMethodGrpCd;    // 납부 수단 그룹

    private String chgbillFailDesc;          // 실패 사유
    private String chgbillDelinquentDt;      // 연체일자
    private Long chgbillDelinquentAmount;    // 연체금액

}