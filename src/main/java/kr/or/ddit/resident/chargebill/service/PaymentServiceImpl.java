package kr.or.ddit.resident.chargebill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.mapper.ChargeBillMapper;
import kr.or.ddit.vo.ChargeBillVO;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private  ChargeBillMapper mapper;

	@Override
	public List<ChargeBillVO> retrieveChargeBillListForMonths(String unitId, String currentMonth, String previousMonth) {
	    // 전월 및 전전월의 청구 내역 조회
	    List<ChargeBillVO> chargeBillListCurrentMonth = mapper.selectChargeBillForMonths(unitId, currentMonth, previousMonth);
	    return chargeBillListCurrentMonth;
	}

	@Override
	public List<ChargeBillVO> selectChargeBillDetail(String unitId, String chargeMonth) {
		// TODO Auto-generated method stub
		return mapper.selectChargeBillDetail(unitId, chargeMonth);
	}
	
}
