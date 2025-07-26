package kr.or.ddit.building.chargeBill.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.chargeBill.dto.ChargeBillHistoryDTO;
import kr.or.ddit.building.chargeBill.dto.EnergyUsageDTO;
import kr.or.ddit.building.mapper.PaymentsReceiptMapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.UnitVO;

@Service
public class PaymentsReceiptServiceImpl implements PaymentsReceiptService {

	@Autowired
	PaymentsReceiptMapper mapper;

	@Override
	public String getRentalPtyId(String mbrCd) {

		return mapper.selectRentalPtyId(mbrCd);
	}

	@Override
	public List<BuildingVO> getOwnBuildings(String rentalPtyId) {

		return mapper.selectOwnBuildings(rentalPtyId);
	}

	@Override
	public List<UnitVO> getUnits(String bldgId, String rentalPtyId) {

		return mapper.selectUnits(bldgId, rentalPtyId);
	}

	@Override
	public int getChargeBillHistoryCount(ChargeBillHistoryDTO cbhDTO) {
		// TODO Auto-generated method stub
		return mapper.selectChargeBillHistoryCount(cbhDTO);
	}

	@Override
	public List<ChargeBillHistoryDTO> getChargeBillHistoryPaged(ChargeBillHistoryDTO cbhDTO, int startRow, int endRow) {
		// TODO Auto-generated method stub
		return mapper.selectChargeBillHistoryPaged(cbhDTO, startRow, endRow);
	}

	@Override
	public List<Map<String, Object>> getMonthlyChargeBillSummary(String rentalPtyId, String chgbillChargeMonth) {
	    return mapper.selectMonthlyChargeBillSummary(rentalPtyId, chgbillChargeMonth);
	}

	@Override
	public List<EnergyUsageDTO> getEnergyUsage(ChargeBillHistoryDTO cbhDTO) {
		// TODO Auto-generated method stub
		return null;
	}

}
