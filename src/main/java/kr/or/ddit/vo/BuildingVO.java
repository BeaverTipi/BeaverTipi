package kr.or.ddit.vo;

import java.math.BigDecimal;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"rentalPtyId", "bldgId"})
public class BuildingVO {
	private String bldgTypeGroupCd;
	private String bldgId;
	private String rentalPtyId;
	private String bldgNm;
	private String bldgZipNo;
	private String bldgAddr;
	private String bldgDtlAddr;
	private String bldgTypeCode;
	private Integer bldgFlrCnt;
	private Integer bldgUnitCnt;
	private String bldgCmpltnDt;
	private Integer bldgGrossArea;
	private String bldgImgPath;

    
    private TenancyVO tenancyInfo;
}