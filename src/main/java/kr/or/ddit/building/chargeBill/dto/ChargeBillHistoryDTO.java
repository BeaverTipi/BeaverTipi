package kr.or.ddit.building.chargeBill.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ChargeBillHistoryDTO implements Serializable{
	@JsonProperty 
	public String chgbillChargeMonth;
	
	public String rentalPtyId;
	public String unitId;
	public String bldgId;
	public Long chgbillAmount;
	public String chgbillStatus;
	public String chgbillDate;
	public String chgbillDueDate;
	public String chgbillPaidDate;
	public String chgbillDesc;
	public String chgbillAccNum;
	public Long chgbillPayAmount;
	public String bldgNm;
	public String unitFlrNo;
	public String unitRoom;
	public String mbrNm;
	
	public LocalDate chgbillDueStartDate;
	public LocalDate chgbillDueEndDate;
	
}

