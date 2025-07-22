package kr.or.ddit.building.chargeBill.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ChargeBillCreateDTO implements Serializable {
    private String rentalPtyId;       // 임대 주체 ID
    private String unitId;            // 세대 ID
    private String bldgId;            // 건물 ID
    private Long chgbillAmount;       // 청구 총금액
    private String chgbillDueDate;    // 납부기한 (yyyyMMdd)
    private String chgbillDesc;       // 청구 설명 (공통 + 개인)
    private String chgbillAccNum;     // 입금 계좌번호
}