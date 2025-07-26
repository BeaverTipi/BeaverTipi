package kr.or.ddit.resident.chargebill.dto;

import lombok.Data;

@Data
public class PaymentConfirmRequest {
    private String chgbillChargeMonth;
    private String rentalPtyId;
    private String unitId;
    private String bldgId;
    private int amount;
    private String chgbillId;
    private String paymentKey;
    private String method;
    private String methodGrpCd;
}