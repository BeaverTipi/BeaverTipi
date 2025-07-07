package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="unitId")
public class ChargeBillVO implements Serializable{
	private String chgbillStatusCode;
	private Integer chgbillCurrentVer;
	private String chgbillDueDate;
	private String chgbillDesc;
	private String chgbillNm;
	private String chgbillDt;
	private String chgbillPck;
	private String unitMntnFeeCd;
	private String chgbillId;
	private String unitId;
	private String bldgId;
	private String chgbillChargeMonth;
	private Integer chgbillCalcAmount;
}
