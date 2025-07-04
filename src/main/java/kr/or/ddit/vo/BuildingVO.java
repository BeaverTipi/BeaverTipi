package kr.or.ddit.vo;

import java.math.BigDecimal;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"rentalPtyId", "bldgId", "unitId"})
public class BuildingVO {
   
	// 기본 세대 정보
    private String unitId;
    private String bldgId;
    private String rentalPtyId;
    private Integer unitFlrNo;
    private Integer unitCmar;
    private Integer unitXuar;
    private BigDecimal unitDsrMnthRentAmt;
    private BigDecimal unitDsrSaleAmt;
    private BigDecimal unitDpstAmt;
    private String unitDtlDescCn;
    private String unitStatCd;

    // 추가 정보 (JOIN)
    private String bldgNm;
    private String bldgImgPath;

    // JSP에서 바인딩하는 필드들
    private String bldgZipNo;
    private String bldgAddr;
    private String bldgDtlAddr;
    private String bldgTypeCode;
    private String bldgCmpltnDt;
    private Integer bldgFlrCnt;
    private Integer bldgUnitCnt;
    private BigDecimal bldgGrossArea;
    
    private TenancyVO tenancyInfo;
}