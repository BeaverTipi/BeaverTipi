package kr.or.ddit.resident.checkPage.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.checkPage.dto.CheckComparisonDto;
import kr.or.ddit.resident.mapper.ChargeBillMapper;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentCheckServiceImpl implements PaymentCheckService {

    @Autowired
    private ChargeBillMapper mapper;

    @Autowired
    private UnitResidentService unitResidentService;
    
    private static final Map<String, String> ENERGY_TYPE_MAP = Map.of(
    	    "001", "전기",
    	    "002", "가스",
    	    "003", "수도"
    	);
    
    @Override	
    public List<CheckComparisonDto> getMonthlyCharges(String unitId, String chargeMonth) {
        List<Map<String, Object>> rawList = mapper.selectChargeBillComparisonDetail(unitId, List.of(chargeMonth));
        Map<String, CheckComparisonDto> resultMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rawList) {
            String feeCode     = String.valueOf(row.get("INT_MAN_FEE_CD"));
            String feeName     = String.valueOf(row.get("INTMANFEENAME"));
            String description = String.valueOf(row.get("INTMANFEEDESC"));
            int amount         = getInt(row.get("AMOUNT"));
            
            CheckComparisonDto dto = new CheckComparisonDto();
            dto.setFeeCode(feeCode);
            dto.setFeeName(feeName);
            dto.setDescription(description);
            dto.setChargeMonth(chargeMonth);
            dto.setChargeAmount(amount); // 현재는 전월 기준

            resultMap.put(feeCode, dto);
        }

        return new ArrayList<>(resultMap.values());
    }
	@Override
	public Map<String, Map<String, Object>> getEnergyUsageSummary(String unitId, String month) {
	    List<Map<String, Object>> usageList = mapper.selectEnergyUsageSummary(unitId, List.of(month));
	    Map<String, Map<String, Object>> summary = new LinkedHashMap<>();

	    for (Map<String, Object> row : usageList) {
	        String energyType = String.valueOf(row.get("ENERGYTYPECODE"));
	        int usageQty      = getInt(row.get("USAGEQTY"));
	        int chargeAmt     = getInt(row.get("CHARGEAMT"));

	        summary.computeIfAbsent(month, m -> new LinkedHashMap<>())
	               .put(energyType, Map.of("usageQty", usageQty, "chargeAmt", chargeAmt));
	    }
	    
	    log.info("📊 에너지 요약 정보: {}", summary);
	    return summary;
	}

	private int getInt(Object value) {
	    if (value instanceof Number) {
	        return ((Number) value).intValue();
	    } else if (value instanceof String) {
	        try {
	            return Integer.parseInt((String) value);
	        } catch (NumberFormatException e) {
	            return 0;
	        }
	    }
	    return 0;
	}
	
	@Override
	public List<CheckComparisonDto> getMonthlyComparison(String unitId, String twoMonthsAgo, String previousMonth) {
	    List<Map<String, Object>> rawList = mapper.selectChargeBillComparisonDetail(unitId, List.of(previousMonth, twoMonthsAgo));
	    Map<String, CheckComparisonDto> resultMap = new LinkedHashMap<>();

	    for (Map<String, Object> row : rawList) {
	        String feeCode     = String.valueOf(row.get("INT_MAN_FEE_CD"));
	        String feeName     = String.valueOf(row.get("INTMANFEENAME"));
	        String description = String.valueOf(row.get("INTMANFEEDESC"));
	        String month       = String.valueOf(row.get("CHARGEMONTH"));
	        int amount         = getInt(row.get("AMOUNT"));

	        CheckComparisonDto dto;

	        if (!resultMap.containsKey(feeCode)) {
	            dto = new CheckComparisonDto();
	            dto.setFeeCode(feeCode);
	            dto.setFeeName(feeName);
	            dto.setDescription(description);
	        } else {
	            dto = resultMap.get(feeCode);
	        }
	        
	        dto.setFeeCode(feeCode);
	        dto.setFeeName(feeName);
	        dto.setDescription(description);
	        
	        if (month.equals(previousMonth)) {
	            dto.setPreviousAmount(dto.getPreviousAmount() + amount);
	        } else if (month.equals(twoMonthsAgo)) {
	            dto.setTwoMonthsAgo(dto.getTwoMonthsAgo() + amount);
	            dto.setChargeMonth(twoMonthsAgo); 
	        }

	        log.info("➡️ feeCode: {}, feeName: {}, month: {}, amount: {}", feeCode, feeName, month, amount);

	        resultMap.put(feeCode, dto);
	    }

	    // ⬇️ 증감 계산 로직
	    for (CheckComparisonDto dto : resultMap.values()) {
	    	int diff = dto.getPreviousAmount() - dto.getTwoMonthsAgo();
	        dto.setDiffAmount(diff);
	        dto.setPercentChange(dto.getPreviousAmount() != 0 ? (int)((diff * 100.0) / dto.getPreviousAmount()) : 0);
	    }

	    return new ArrayList<>(resultMap.values()); // ✅ 반드시 추가
	}
	
	@Override
	public Map<String, Map<String, Object>> getEnergyComparison(String unitId, String currentMonth, String previousMonth) {
	    List<Map<String, Object>> rawList = mapper.selectEnergyUsageSummary(unitId, List.of(previousMonth, currentMonth));
	    Map<String, Map<String, Object>> result = new LinkedHashMap<>();

	    for (Map<String, Object> row : rawList) {
	        String month       = String.valueOf(row.get("CHARGEMONTH"));
	        String energyTypeCode = String.valueOf(row.get("ENERGYTYPECODE"));
	        String energyType  = ENERGY_TYPE_MAP.getOrDefault(energyTypeCode, "기타");
	        int usageQty       = getInt(row.get("USAGEQTY"));
	        int chargeAmt      = getInt(row.get("CHARGEAMT"));

	        result.computeIfAbsent(month, k -> new LinkedHashMap<>())
	              .put(energyType, Map.of("usageQty", usageQty, "chargeAmt", chargeAmt));
	    }
	    
	    log.info("📊 DEBUG: rawList = {}", rawList);
	    log.info("📊 DEBUG: result = {}", result);
	    return result;
	}
	@Override
	public List<UnitResidentVO> getMyUnitsInBuilding(String mbrCd, String bldgId) {
		return unitResidentService.selectMyUnitsInBuilding(mbrCd, bldgId);
	}
	@Override
	public List<String> getAvailableChargeMonths(String unitId) {
		// TODO Auto-generated method stub
		return mapper.selectChargeBillAvailableMonths(unitId);
	}


}
