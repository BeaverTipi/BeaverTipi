package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"unitId","bldgId","rentalPtyId"})
public class HouseholdEnergyMonthlyUsageVO implements Serializable{
	private String unitId;
	private String bldgId;
	private String rentalPtyId;
	private String dumMonth;
	private String dumComp;
	private Integer totalEnergyChargeAmt;
	private Integer totalEnergyUsageQty;
	private String enegType;
	private String meterReadDt;
}
