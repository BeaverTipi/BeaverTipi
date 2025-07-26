package kr.or.ddit.resident.chargebill.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.resident.chargebill.dto.ChargeComparisonDto;
import kr.or.ddit.resident.chargebill.dto.PaymentConfirmRequest;
import kr.or.ddit.resident.mapper.ChargeBillMapper;
import kr.or.ddit.resident.mapper.UnitResidentMapper;
import kr.or.ddit.vo.ChargeBillVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private ChargeBillMapper mapper;

    @Autowired
    private UnitResidentMapper unitResidentMapper;
    
    @Override
    public List<Map<String, Object>> selectChargeBillComparisonDetail(String unitId, String twoMonthsAgo, String previousMonth) {
        return mapper.selectChargeBillComparisonDetail(unitId, List.of(twoMonthsAgo, previousMonth));
    }

    @Override
    public List<ChargeComparisonDto> getChargeComparisonList(String unitId, String twoMonthsAgo, String previousMonth) {
        List<String> months = List.of(twoMonthsAgo, previousMonth);
        List<Map<String, Object>> rawList = mapper.selectChargeBillComparisonDetail(unitId, months);
        log.info("📦 rawList={}", rawList);
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

            if (month.equals(twoMonthsAgo)) {
                dto.setTwoMonthsAgo(dto.getTwoMonthsAgo() + amount);
//                dto.setEnergyUsageTwoMonthsAgo(dto.getEnergyUsageTwoMonthsAgo() + usageQty);
            } else if (month.equals(previousMonth)) {
                dto.setPreviousAmount(dto.getPreviousAmount() + amount);
//                dto.setEnergyUsagePrevious(dto.getEnergyUsagePrevious() + usageQty);
            }
            log.info("✅ DTO feeName={}, getPreviousAmount={}", dto.getFeeName(), dto.getPreviousAmount());
            resultMap.put(feeCode, dto);
        }

        for (ChargeComparisonDto dto : resultMap.values()) {
            int diff = dto.getPreviousAmount() - dto.getTwoMonthsAgo();
            dto.setDiffAmount(diff);
            dto.setEnergyUsageDiffPercent(dto.getPreviousAmount() != 0 ? (int)((diff * 100.0) / dto.getPreviousAmount()) : 0);
        }

        return new ArrayList<>(resultMap.values());
    }

    @Override
    public Map<String, Map<String, Object>> getEnergyUsageSummary(String unitId, String twoMonthsAgo, String previousMonth) {
        List<String> months = List.of(twoMonthsAgo, previousMonth);
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

	@Override
	public List<UnitResidentVO> selectMyUnitsInBuilding(String bldgId, String mbrCd) {
		return unitResidentMapper.selectMyUnitsInBuilding(mbrCd, bldgId);
	}

	@Override
	@Transactional
	public void payChargeBill(PaymentConfirmRequest dto) {

		// 💡 납부 초과 방지 체크
		ChargeBillVO bill = mapper.selectChargeBillInfo(dto.getChgbillChargeMonth(),dto.getUnitId());
		if (bill == null) {
			throw new IllegalStateException("💥 청구 정보가 존재하지 않습니다.");
		}
		Long currentPaid = bill.getChgbillAmount();
		Long totalAmount = bill.getChgbillPayAmount();
		if (currentPaid + dto.getAmount() > totalAmount) {
			throw new IllegalArgumentException("💥 납부 금액이 총 청구 금액을 초과할 수 없습니다.");
		}

		// 1. 납부 금액 업데이트
		int updated = mapper.updateChargeBillAfterPayment(dto.getChgbillChargeMonth(),dto.getRentalPtyId(),dto.getUnitId() ,dto.getBldgId(),dto.getAmount());
		if (updated == 0) {
			throw new IllegalStateException("💥 청구서 업데이트 실패 - 정보가 없거나 이미 납부 완료 상태입니다.");
		}

		// 2. 납부 로그 기록
		int inserted = mapper.insertChargeBillPaymentLog(dto.getChgbillId(),dto.getRentalPtyId(),dto.getUnitId() ,dto.getBldgId() ,dto.getPaymentKey() ,dto.getAmount(),
				dto.getMethod(),dto.getMethodGrpCd());
		if (inserted == 0) {
			throw new IllegalStateException("💥 납부 로그 기록 실패");
		}

		log.info("💸 납부 처리 완료: unitId={}, amount={}, status=성공", dto.getUnitId(), dto.getAmount());
	}

	@Override
	public Long getCurrentChargeAmount(String unitId,String chargeMonth) {
		log.info("📥 [getCurrentChargeAmount] unitId={}, chargeMonth={}", unitId,  chargeMonth);
		
		ChargeBillVO bill = mapper.selectChargeBillInfo(chargeMonth, unitId);
	    if (bill == null) {
	        log.warn("❗ 청구 정보가 없습니다.");
	        return 0L;
	    }
	    Long total = bill.getChgbillAmount();
	    Long paid = bill.getChgbillPayAmount();
	    
	    log.info("💰 [getCurrentChargeAmount] 총 청구금액={}, 납부된 금액={}, 남은 금액={}", total, paid, total - paid);
	    return total - paid;
	}

	@Override
	public void payChargeBill(String mbrCd) {
	    List<ChargeBillVO> unpaidBills = mapper.selectUnpaidChargeBills(mbrCd);

	    for (ChargeBillVO bill : unpaidBills) {
	        int remainingAmount = getInt(bill.getChgbillAmount()) - getInt(bill.getChgbillPayAmount());

	        if (remainingAmount > 0) {
	            mapper.updateChargeBillAfterPayment(
	                bill.getChgbillChargeMonth(),
	                bill.getRentalPtyId(),
	                bill.getUnitId(),
	                bill.getBldgId(),
	                remainingAmount
	            );

	            mapper.insertChargeBillPaymentLog(
	                bill.getChgbillChargeMonth(), // 만약 chgbillId가 없다면 생성 로직 필요
	                bill.getRentalPtyId(),
	                bill.getUnitId(),
	                bill.getBldgId(),
	                "BULK_SUCCESS_" + System.currentTimeMillis(),
	                remainingAmount,
	                "SYSTEM",
	                "BULK"
	            );
	        }
	    }
	}


	

}
