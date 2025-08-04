package kr.or.ddit.building.main.dto;

import lombok.Data;

@Data
public class ChargeBillAmountDTO {
	public int currentMonthChgbillTotal;
	public int lastMonthChgbillTotal;
	public int lastYearAgoChgbillTotal;
}
