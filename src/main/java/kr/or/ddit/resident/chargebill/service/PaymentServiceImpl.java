package kr.or.ddit.resident.chargebill.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.main.subscribe.service.SubscribeSubsriptionService;
import kr.or.ddit.resident.chargebill.dto.ChargeComparisonDto;
import kr.or.ddit.resident.chargebill.dto.PaymentConfirmRequest;
import kr.or.ddit.resident.mapper.ChargeBillMapper;
import kr.or.ddit.resident.mapper.UnitResidentMapper;
import kr.or.ddit.vo.CardVO;
import kr.or.ddit.vo.ChargeBillPaymentLogVO;
import kr.or.ddit.vo.ChargeBillVO;
import kr.or.ddit.vo.EasyPayVO;
import kr.or.ddit.vo.PaymentTosspamentsRawVO;
import kr.or.ddit.vo.UnitResidentVO;
import kr.or.ddit.vo.VirtualAccountVO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private ChargeBillMapper mapper;

    @Autowired
    private UnitResidentMapper unitResidentMapper;
    
    @Autowired
    private SubscribeSubsriptionService service;
    
    private static final Map<String, String> METHOD_CODE_MAP = Map.of(
    	    "카드", "001",
    	    "계좌이체", "002",
    	    "가상계좌", "003"
    	);
    
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
                dto.setEnergyUsageTwoMonthsAgo(dto.getEnergyUsageTwoMonthsAgo() + usageQty);
            } else if (month.equals(previousMonth)) {
                dto.setPreviousAmount(dto.getPreviousAmount() + amount);
                dto.setEnergyUsagePrevious(dto.getEnergyUsagePrevious() + usageQty);
            }
            log.info("✅ DTO feeName={}, getPreviousAmount={}", dto.getFeeName(), dto.getPreviousAmount());
            resultMap.put(feeCode, dto);
        }

        for (ChargeComparisonDto dto : resultMap.values()) {
        	int diff = dto.getTwoMonthsAgo() - dto.getPreviousAmount(); 
            dto.setDiffAmount(diff);
            dto.setEnergyUsageDiffPercent(dto.getEnergyUsageTwoMonthsAgo() != 0 ? (int)((diff * 100.0) / dto.getEnergyUsageTwoMonthsAgo()) : 0);
            log.info("🧾 FEE: {}, 전전월={}, 전월={}, diff={}",
                    dto.getFeeName(), dto.getTwoMonthsAgo(), dto.getPreviousAmount(), dto.getDiffAmount());

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
        return value != null ? ((Number) value).intValue() : 0;
    }

	@Override
	public List<UnitResidentVO> selectMyUnitsInBuilding(String bldgId, String mbrCd) {
		return unitResidentMapper.selectMyUnitsInBuilding(mbrCd, bldgId);
	}

//	@Override
//	@Transactional
//	public void payChargeBill(PaymentConfirmRequest dto) {
//
//		// 💡 납부 초과 방지 체크
//		ChargeBillVO bill = mapper.selectChargeBillInfo(dto.getChgbillChargeMonth(),dto.getUnitId());
//		if (bill == null) {
//			throw new IllegalStateException("💥 청구 정보가 존재하지 않습니다.");
//		}
//		Long totalAmount = bill.getChgbillAmount();         // 총 청구 금액
//		Long currentPaid = bill.getChgbillPayAmount();      // 현재까지 납부된 금액
//		if (currentPaid + dto.getAmount() > totalAmount) {
//		    throw new IllegalArgumentException("💥 납부 금액이 총 청구 금액을 초과할 수 없습니다.");
//		}
//
//		// 1. 납부 금액 업데이트
//		int updated = mapper.updateChargeBillAfterPayment(dto.getChgbillChargeMonth(),dto.getRentalPtyId(),dto.getUnitId() ,dto.getBldgId(),dto.getAmount());
//		if (updated == 0) {
//			throw new IllegalStateException("💥 청구서 업데이트 실패 - 정보가 없거나 이미 납부 완료 상태입니다.");
//		}
//
//		// 2. 납부 로그 기록
//		int inserted = mapper.insertChargeBillPaymentLog(dto.getChgbillId(),dto.getRentalPtyId(),dto.getUnitId() ,dto.getBldgId() ,dto.getPaymentKey() ,dto.getAmount(),
//				dto.getMethod(),dto.getMethodGrpCd());
//		if (inserted == 0) {
//			throw new IllegalStateException("💥 납부 로그 기록 실패");
//		}
//
//		log.info("💸 납부 처리 완료: unitId={}, amount={}, status=성공", dto.getUnitId(), dto.getAmount());
//	}

	@Override
	public int getCurrentChargeAmount(String unitId,String chargeMonth) {
		log.info("📥 [getCurrentChargeAmount] unitId={}, chargeMonth={}", unitId,  chargeMonth);
		
		ChargeBillVO bill = mapper.selectChargeBillInfo(chargeMonth, unitId);
	    if (bill == null) {
	        log.warn("❗ 청구 정보가 없습니다.");
	        return 0;
	    }
	    int total = bill.getChgbillAmount();
	    int paid = bill.getChgbillPayAmount();
	    
	    log.info("💰 [getCurrentChargeAmount] 총 청구금액={}, 납부된 금액={}, 남은 금액={}", total, paid, total - paid);
	    return total - paid;
	}
//
//	@Override
//	public void payChargeBill(String mbrCd) {
//	    List<ChargeBillVO> unpaidBills = mapper.selectUnpaidChargeBills(mbrCd);
//
//	    for (ChargeBillVO bill : unpaidBills) {
//	        int remainingAmount = getInt(bill.getChgbillAmount()) - getInt(bill.getChgbillPayAmount());
//
//	        if (remainingAmount > 0) {
//	            mapper.updateChargeBillAfterPayment(
//	                bill.getChgbillChargeMonth(),
//	                bill.getRentalPtyId(),
//	                bill.getUnitId(),
//	                bill.getBldgId(),
//	                remainingAmount
//	            );
//
//	            mapper.insertChargeBillPaymentLog(
//	                bill.getChgbillChargeMonth(), // 만약 chgbillId가 없다면 생성 로직 필요
//	                bill.getRentalPtyId(),
//	                bill.getUnitId(),
//	                bill.getBldgId(),
//	                "BULK_SUCCESS_" + System.currentTimeMillis(),
//	                remainingAmount,
//	                "SYSTEM",
//	                "BULK"
//	            );
//	        }
//	    }
//	}
	@Override
	@Transactional
	public void confirmAndPayFromToss(Map<String, Object> data,
							            String approvedAtRaw,
							            LocalDate approvedAt,
							            String mbrCd,
							            Map<String, Object> cardMap,
							            Map<String, Object> easyPayMap,
							            Map<String, Object> vaMap,
							            String unitId,
							            String chgbillChargeMonth) {
	    log.info("🧾 [confirmAndPayFromToss] 토스 응답 저장 및 납부 처리 시작");
	    
	    PaymentTosspamentsRawVO paymentTosspamentsRawVO = new PaymentTosspamentsRawVO();
	    
	   
	    
	    // 💾 [1] Toss Raw 응답 저장
	    PaymentTosspamentsRawVO paymentToss = new PaymentTosspamentsRawVO();
	    paymentToss.setPaymentKey((String) data.get("paymentKey"));
	    paymentToss.setVersionDate(approvedAtRaw); // 또는 data.get("requestedAt") 도 가능
	    paymentToss.setType((String) data.get("type"));
	    paymentToss.setOrderId((String) data.get("orderId"));
	    paymentToss.setOrderName((String) data.get("orderName"));
	    paymentToss.setMId((String) data.get("mId"));
	    paymentToss.setCurrency((String) data.get("currency"));
	    paymentToss.setMethod((String) data.get("method"));
	    paymentToss.setTotalAmount((Integer) data.get("totalAmount"));
	    paymentToss.setStatus((String) data.get("status"));
	    paymentToss.setRequestDate((String) data.get("requestedAt"));
	    paymentToss.setApproveDate(approvedAtRaw);
	    paymentToss.setLastTransactionKey((String) data.get("lastTransactionKey"));
	    paymentToss.setSuppliedAmount((Integer) data.get("suppliedAmount"));
	    paymentToss.setCountry((String) data.get("country"));
//	    paymentToss.setMbrCd(mbrCd); // 필요 시 VO에 추가 필드 생성

	    mapper.insertTossPaymentInfo(paymentToss); // 💾 Toss 응답 raw 저장 쿼리
	    
	    ChargeBillVO chargeBillVO = mapper.selectChargeBillInfo(chgbillChargeMonth,unitId);
	    chargeBillVO.setChgbillPayAmount((Integer)data.get("totalAmount"));
	    
	    int result = mapper.updateChargeBillAfterPayment(
	    		chargeBillVO
	    		);
	    
	    ChargeBillPaymentLogVO chargeBillPaymentLogVO = new ChargeBillPaymentLogVO();
	    chargeBillPaymentLogVO.setChgbillChargeMonth(chgbillChargeMonth);
	    chargeBillPaymentLogVO.setRentalPtyId(chargeBillVO.getRentalPtyId());
	    chargeBillPaymentLogVO.setUnitId(chargeBillVO.getUnitId());
	    chargeBillPaymentLogVO.setBldgId(chargeBillVO.getBldgId());

	    chargeBillPaymentLogVO.setPaymentKey((String) data.get("paymentKey"));
	    chargeBillPaymentLogVO.setChgbillPayAmount(((Number) data.get("totalAmount")).longValue());
	    chargeBillPaymentLogVO.setChgbillPaidAt(new Date()); // 또는 approvedAt → java.util.Date 변환

	    String method = (String) data.get("method");
	    String methodCd = METHOD_CODE_MAP.getOrDefault(method, "000"); // 000 = 알 수 없음
	    chargeBillPaymentLogVO.setChgbillPayMethod(methodCd);
	    chargeBillPaymentLogVO.setChgbillPayMethodGrpCd("PAYTP");

	    chargeBillPaymentLogVO.setChgbillFailDesc(null);              // 실패 없으면 null
	    chargeBillPaymentLogVO.setChgbillDelinquentDt(null);          // 연체일자 없음
	    chargeBillPaymentLogVO.setChgbillDelinquentAmount(0L);        // 연체금 없음

	    mapper.insertChargeBillPaymentLog(chargeBillPaymentLogVO);
	    
	    if( cardMap !=null && !cardMap.isEmpty() ) {
        	CardVO card = new CardVO();
        	card.setIssuerCode(cardMap.get("issuerCode").toString());
        	card.setAcquirerCode(cardMap.get("acquirerCode").toString());
        	card.setCardNumber(cardMap.get("number").toString());
        	card.setCardType(cardMap.get("cardType").toString());
        	card.setOwnerType(cardMap.get("ownerType").toString());
        	card.setMbrCd(mbrCd);
        	service.createCard(card);
        }
        if(easyPayMap != null && !easyPayMap.isEmpty()) {
        	EasyPayVO easyPay = new EasyPayVO();	
        	easyPay.setPaymentKey((String) data.get("paymentKey"));
        	easyPay.setProvider(easyPayMap.get("provider").toString());
        	easyPay.setAmount((Integer)easyPayMap.get("amount"));
        	easyPay.setDiscountAmount((Integer)easyPayMap.get("discountAmount"));
        	easyPay.setMbrCd(mbrCd);
        	service.createEasyPay(easyPay);
        }
        if (vaMap != null && !vaMap.isEmpty()) {
        	VirtualAccountVO va = new VirtualAccountVO();
        	va.setAccountNumber((String) vaMap.get("accountNumber"));
        	va.setBankCode((String) vaMap.get("bankCode"));
        	va.setCustomerName((String) vaMap.get("customerName"));
        	va.setAccountType((String) vaMap.get("accountType"));
        	va.setDueDate((String) vaMap.get("dueDate"));
        	va.setExpired((String) vaMap.get("expired"));
        	va.setSettlementStatus((String) vaMap.get("settlementStatus"));
        	va.setSecret((String) vaMap.get("secret"));
        	va.setMbrCd(mbrCd);
        	service.createVirtualAccount(va);
        }

	   }
	    
}


	


