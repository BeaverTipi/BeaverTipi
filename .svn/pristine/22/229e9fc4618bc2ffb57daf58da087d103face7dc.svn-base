package kr.or.ddit.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"rentalPtyId", "bldgId"})
public class BuildingVO implements Serializable{
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
	private String bldgImgPath;// 기본 세대 정보
    private String delYn;
    private TenancyVO tenancyInfo;
    private List<TenancyAccountVO> accList;
    private String accNum; // 계좌번호만 따로주면 좀 더 편하지 않을깡?ㅎ
}