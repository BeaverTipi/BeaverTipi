package kr.or.ddit.resident.chargebill.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.chargebill.dto.ChargeComparisonDto;
import kr.or.ddit.resident.mapper.ChargeBillMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private ChargeBillMapper mapper;

    @Override
    public List<Map<String, Object>> selectChargeBillComparisonDetail(String unitId, String currentMonth, String previousMonth) {
        return mapper.selectChargeBillComparisonDetail(unitId, List.of(currentMonth, previousMonth));
    }

    @Override
    public List<ChargeComparisonDto> getChargeComparisonList(String unitId, String currentMonth, String previousMonth) {
        List<String> months = List.of(currentMonth, previousMonth);
        List<Map<String, Object>> rawList = mapper.selectChargeBillComparisonDetail(unitId, months);

        log.info("✅ 청구 비교 rawList size: {}", rawList.size());
        Map<String, ChargeComparisonDto> resultMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rawList) {
            String feeCode     = String.valueOf(row.get("INT_MAN_FEE_CD"));
            String feeName     = String.valueOf(row.get("INTMANFEENAME"));
            String description = String.valueOf(row.get("INTMANFEEDESC"));
            String month       = String.valueOf(row.get("CHARGEMONTH"));
            int amount         = getInt(row.get("AMOUNT"));
            int usageQty       = getInt(row.get("ENERGYUSAGEQTY"));

            log.info("🔍 rawRow: month={}, feeCode={}, feeName={}, amount={}, usageQty={}",
                    month, feeCode, feeName, amount, usageQty);
            
            ChargeComparisonDto dto = resultMap.getOrDefault(feeCode, new ChargeComparisonDto());
            dto.setFeeName(feeName);
            dto.setDescription(description);

            if (month.equals(previousMonth)) {
                dto.setPreviousAmount(dto.getPreviousAmount() + amount);
                dto.setEnergyUsageCurrent(dto.getEnergyUsageCurrent() + usageQty);
            } else if (month.equals(currentMonth)) {
                dto.setCurrentAmount(dto.getCurrentAmount() + amount);
            }
            log.info("✅ DTO feeName={}, currentAmount={}", dto.getFeeName(), dto.getCurrentAmount());
            resultMap.put(feeCode, dto);
        }

        for (ChargeComparisonDto dto : resultMap.values()) {
            int diff = dto.getCurrentAmount() - dto.getPreviousAmount();
            dto.setDiffAmount(diff);
            dto.setEnergyUsageDiffPercent(dto.getPreviousAmount() != 0 ? (int)((diff * 100.0) / dto.getPreviousAmount()) : 0);
        }

        return new ArrayList<>(resultMap.values());
    }

    @Override
    public Map<String, Map<String, Object>> getEnergyUsageSummary(String unitId, String currentMonth, String previousMonth) {
        List<String> months = List.of(currentMonth, previousMonth);
        List<Map<String, Object>> usageList = mapper.selectEnergyUsageSummary(unitId, months);

        Map<String, Map<String, Object>> summary = new LinkedHashMap<>();

        for (Map<String, Object> row : usageList) {
            String month      = String.valueOf(row.get("CHARGEMONTH"));
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
}
