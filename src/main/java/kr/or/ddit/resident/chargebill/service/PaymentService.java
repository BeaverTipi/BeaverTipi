package kr.or.ddit.resident.chargebill.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import kr.or.ddit.resident.chargebill.dto.ChargeComparisonDto;
import kr.or.ddit.resident.chargebill.dto.PaymentConfirmRequest;
import kr.or.ddit.vo.ChargeBillVO;
import kr.or.ddit.vo.PaymentTosspamentsRawVO;
import kr.or.ddit.vo.UnitResidentVO;
public interface PaymentService {
	
	public List<Map<String, Object>> selectChargeBillComparisonDetail(
	        String unitId,
	        String twoMonthsAgo,
	        String previousMonth
	    );

	public List<ChargeComparisonDto> getChargeComparisonList(
			  String unitId,
		        String twoMonthsAgo,
		        String previousMonth
		        
			);
	
	public Map<String, Map<String, Object>> getEnergyUsageSummary(String unitId, String twoMonthsAgo, String previousMonth);

	public List<UnitResidentVO> selectMyUnitsInBuilding(String bldgId, String mbrCd);
	
//    public void payChargeBill(PaymentConfirmRequest dto);
    
    public int getCurrentChargeAmount(String unitId, String chargeMonth);
    
//    public void payChargeBill(String mbrCd);

	void confirmAndPayFromToss(Map<String, Object> data, String approvedAtRaw, LocalDate approvedAt, String mbrCd,
			Map<String, Object> cardMap, Map<String, Object> easyPayMap, Map<String, Object> vaMap, String unitId,
			String chgbillChargeMonth);


    
}
	