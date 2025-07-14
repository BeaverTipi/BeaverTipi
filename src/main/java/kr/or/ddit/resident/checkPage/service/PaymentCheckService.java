package kr.or.ddit.resident.checkPage.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.resident.chargebill.dto.ChargeComparisonDto;
import kr.or.ddit.resident.checkPage.dto.CheckComparisonDto;
public interface PaymentCheckService {
	
	   List<CheckComparisonDto> getMonthlyCharges(String unitId, String month);

	   Map<String, Map<String, Object>> getEnergyUsageSummary(String unitId, String month);

	   List<CheckComparisonDto> getMonthlyComparison(String unitId,String currentMonth, String previousMonth);

	   Map<String, Map<String, Object>> getEnergyComparison(String unitId, String currentMonth, String previousMonth);
}
	