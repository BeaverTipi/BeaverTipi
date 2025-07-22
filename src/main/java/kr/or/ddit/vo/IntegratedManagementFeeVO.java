package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of= {"intgFeeId","intManFeeCd","unitId","bldgId","rentalPtyId"})
public class IntegratedManagementFeeVO implements Serializable{
	private String intgFeeId;
	private String unitId;
	private String bldgId;
	private String rentalPtyId;
	private Integer intgFeeAmount;
	private String intgFeeStatus;
	private String intgFeeStatusGrpCd;
	private String intgFeeDueDate;
	private String intgFeePaidDate;
	private String intgFeeIssueDate;
	private String intgFeeDesc;
	private String intManFeeCd;
	private String intManFeeCdGrpCd;

}
