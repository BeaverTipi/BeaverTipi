package kr.or.ddit.resident.chargebill.service;

import java.util.List;

import kr.or.ddit.vo.ChargeBillVO;

public interface PaymentService {

	List<ChargeBillVO> retrieveChargeBillList(String unitId, String chargeMonth);
}
	