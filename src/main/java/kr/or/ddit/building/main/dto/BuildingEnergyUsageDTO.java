package kr.or.ddit.building.main.dto;

import lombok.Data;

@Data
public class BuildingEnergyUsageDTO {
	
	public int electricityUsage;
	public int gasUsage;
	public int waterUsage;
}
