package kr.or.ddit.resident.chargebill.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.resident.chargebill.dto.ChargeComparisonDto;
import kr.or.ddit.resident.chargebill.dto.PaymentConfirmRequest;
import kr.or.ddit.vo.ChargeBillVO;
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
	
    public void payChargeBill(PaymentConfirmRequest dto);
    
    public Long getCurrentChargeAmount(String unitId, String chargeMonth);
    
    public void payChargeBill(String mbrCd);
	
}
	