package kr.or.ddit.building.chargeBill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.chargeBill.dto.ChargeBillHistoryDTO;
import kr.or.ddit.building.mapper.PaymentsReceiptMapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.UnitVO;

@Service
public class PaymentsReceiptServiceImpl implements PaymentsReceiptService {

	@Autowired
	PaymentsReceiptMapper mapper;
	
	@Override
	public List<ChargeBillHistoryDTO> getChargeBillHistory(ChargeBillHistoryDTO cbhDTO) {

		return mapper.selectChargeBillHistory(cbhDTO);
	}

	@Override
	public String getRentalPtyId(String mbrCd) {

		return mapper.selectRentalPtyId(mbrCd);
	}

	@Override
	public List<BuildingVO> getOwnBuildings(String rentalPtyId) {

		return mapper.selectOwnBuildings(rentalPtyId);
	}

	@Override
	public List<UnitVO> getUnits(String bldgId) {

		return mapper.selectUnits(bldgId);
	}

}
