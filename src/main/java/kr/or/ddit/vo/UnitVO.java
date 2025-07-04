package kr.or.ddit.vo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of =  {"unitId","bldgId", "rentalPtyId"})
public class UnitVO {

	private String unitId;         // UNIT_ID (PK, 트리거 자동 생성)
    private String bldgId;         // BLDG_ID (FK)
    private String rentalPtyId;    // RENTAL_PTY_ID (FK)

    // 기본 정보
    private Integer unitFlrNo;     // UNIT_FLR_NO - 호실이 위치한 층수
    private BigDecimal unitCmar;   // UNIT_CMAR - 공급면적
    private BigDecimal unitXuar;   // UNIT_XUAR - 전용면적

    private BigDecimal unitDsrMnthRentAmt; // UNIT_DSR_MNTH_RENT_AMT - 월세 예상금액
    private BigDecimal unitDsrSaleAmt;     // UNIT_DSR_SALE_AMT - 매매 예상금액
    private BigDecimal unitDpstAmt;        // UNIT_DPST_AMT - 보증금 예상금액

    private String unitDtlDescCn; // UNIT_DTL_DESC_CN - 상세 설명 (CLOB)
    private String unitStatCd;    // UNIT_STAT_CD - 상태 코드 (예: REGISTERED)
}
=======
package kr.or.ddit.vo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of =  {"unitId","bldgId", "rentalPtyId"})
public class UnitVO {

	private String unitId;         // UNIT_ID (PK, 트리거 자동 생성)
    private String bldgId;         // BLDG_ID (FK)
    private String rentalPtyId;    // RENTAL_PTY_ID (FK)

    // 기본 정보
    private Integer unitFlrNo;     // UNIT_FLR_NO - 호실이 위치한 층수
    private BigDecimal unitCmar;   // UNIT_CMAR - 공급면적
    private BigDecimal unitXuar;   // UNIT_XUAR - 전용면적

    private BigDecimal unitDsrMnthRentAmt; // UNIT_DSR_MNTH_RENT_AMT - 월세 예상금액
    private BigDecimal unitDsrSaleAmt;     // UNIT_DSR_SALE_AMT - 매매 예상금액
    private BigDecimal unitDpstAmt;        // UNIT_DPST_AMT - 보증금 예상금액

    private String unitDtlDescCn; // UNIT_DTL_DESC_CN - 상세 설명 (CLOB)
    private String unitStatCd;    // UNIT_STAT_CD - 상태 코드 (예: REGISTERED)
}
