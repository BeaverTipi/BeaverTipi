package kr.or.ddit.resident.checkPage.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.resident.checkPage.dto.CheckComparisonDto;
import kr.or.ddit.vo.UnitResidentVO;
public interface PaymentCheckService {
	
	   List<CheckComparisonDto> getMonthlyCharges(String unitId, String month);

	   Map<String, Map<String, Object>> getEnergyUsageSummary(String unitId, String month);

	   List<CheckComparisonDto> getMonthlyComparison(String unitId,String twoMonthsAgo, String previousMonth);

	   Map<String, Map<String, Object>> getEnergyComparison(String unitId, String twoMonthsAgo, String previousMonth);

	   List<UnitResidentVO> getMyUnitsInBuilding(String mbrCd, String bldgId);
	   
	// PaymentCheckService
	   List<String> getAvailableChargeMonths(String unitId);

}	
	