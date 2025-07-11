package kr.or.ddit.vo;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of= {"dumCompCd","dumComp","unitId","bldgId","rentalPtyId"})
public class ManagementEntityMonthlyChargeAggregationVO implements Serializable{
	private String dumMonth;
	private String dumComp;
	private String unitId;
	private String bldgId;
	private String rentalPtyId;
	private Integer usageValue;
	private String enegType;
	private String meterReadDt;
	private String enegTypeGrpCd;
}
