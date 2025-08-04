package kr.or.ddit.building.main.dto;

import lombok.Data;

@Data
public class ChargeBillCountDTO {

	public int totalChargeCount;
	public int unpaidCount;
	public int paidCount;
	public int overdueCount;
}
