package kr.or.ddit.resident.checkPage.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.checkPage.dto.CheckComparisonDto;
import kr.or.ddit.resident.mapper.ChargeBillMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentCheckServiceImpl implements PaymentCheckService {

    @Autowired
    private ChargeBillMapper mapper;

    @Override
    public List<CheckComparisonDto> getMonthlyCharges(String unitId, String month) {
        List<Map<String, Object>> rawList = mapper.selectChargeBillComparisonDetail(unitId, List.of(month));
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
            dto.setChargeMonth(month);
            dto.setPreviousAmount(amount); // 현재는 전월 기준

            resultMap.put(feeCode, dto);
        }

        return new ArrayList<>(resultMap.values());
    }
	@Override
	public Map<String, Map<String, Object>> getEnergyUsageSummary(String unitId, String month) {
	    List<Map<String, Object>> usageList = mapper.selectEnergyUsageSummary(unitId, List.of(month));
	    Map<String, Map<String, Object>> summary = new LinkedHashMap<>();

	    for (Map<String, Object> row : usageList) {
	        String energyType = String.valueOf(row.get("ENERGYTYPENAME"));
	        int usageQty      = getInt(row.get("USAGEQTY"));
	        int chargeAmt     = getInt(row.get("CHARGEAMT"));

	        summary.computeIfAbsent(month, m -> new LinkedHashMap<>())
	               .put(energyType, Map.of("usageQty", usageQty, "chargeAmt", chargeAmt));
	    }

	    log.info("📊 에너지 요약 정보: {}", summary);
	    return summary;
	}

	private int getInt(Object value) {
	    return value != null ? ((Number) value).intValue() : 0;
	}
	@Override
	public List<CheckComparisonDto> getMonthlyComparison(String unitId, String currentMonth, String previousMonth) {
	    List<Map<String, Object>> rawList = mapper.selectChargeBillComparisonDetail(unitId, List.of(previousMonth, currentMonth));
	    Map<String, CheckComparisonDto> resultMap = new LinkedHashMap<>();

	    for (Map<String, Object> row : rawList) {
	        String feeCode     = String.valueOf(row.get("INT_MAN_FEE_CD"));
	        String feeName     = String.valueOf(row.get("INTMANFEENAME"));
	        String description = String.valueOf(row.get("INTMANFEEDESC"));
	        String month       = String.valueOf(row.get("CHARGEMONTH"));
	        int amount         = getInt(row.get("AMOUNT"));

	        CheckComparisonDto dto = resultMap.getOrDefault(feeCode, new CheckComparisonDto());
	        dto.setFeeCode(feeCode);
	        dto.setFeeName(feeName);
	        dto.setDescription(description);

	        if (month.equals(previousMonth)) {
	            dto.setPreviousAmount(dto.getPreviousAmount() + amount);
	        } else if (month.equals(currentMonth)) {
	            dto.setChargeAmount(dto.getChargeAmount() + amount);
	            dto.setChargeMonth(currentMonth);
	        }

	        resultMap.put(feeCode, dto);
	    }

	    // ⬇️ 증감 계산 로직
	    for (CheckComparisonDto dto : resultMap.values()) {
	        int diff = dto.getChargeAmount() - dto.getPreviousAmount();
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
	        String energyType  = String.valueOf(row.get("ENERGYTYPENAME"));
	        int usageQty       = getInt(row.get("USAGEQTY"));
	        int chargeAmt      = getInt(row.get("CHARGEAMT"));

	        result.computeIfAbsent(month, k -> new LinkedHashMap<>())
	              .put(energyType, Map.of("usageQty", usageQty, "chargeAmt", chargeAmt));
	    }

	    return result;
	}


}
