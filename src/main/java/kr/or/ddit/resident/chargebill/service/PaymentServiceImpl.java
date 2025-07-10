package kr.or.ddit.resident.chargebill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.mapper.ChargeBillMapper;
import kr.or.ddit.vo.ChargeBillVO;
import lombok.RequiredArgsConstructor;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private  ChargeBillMapper mapper;

	@Override
	public List<ChargeBillVO> retrieveChargeBillList(String unitId, String chargeMonth) {
		return mapper.selectChargeBill(unitId, chargeMonth);
	}
	
}
