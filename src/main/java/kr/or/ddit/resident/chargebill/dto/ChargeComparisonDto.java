package kr.or.ddit.resident.chargebill.dto;

import lombok.Data;

@Data
public class ChargeComparisonDto {

	private String feeName;
	private String description;
	private int previousAmount;
	private int currentAmount;
	private int twoMonthsAgo;
	private int diffAmount;
	private int energyUsageCurrent;
	private int energyUsagePrevious;        
	private int energyUsageTwoMonthsAgo;    
	private int energyUsageDiffPercent;
}
