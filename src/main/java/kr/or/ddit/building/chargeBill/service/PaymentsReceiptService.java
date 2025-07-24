package kr.or.ddit.building.chargeBill.service;

import java.util.List;

import kr.or.ddit.building.chargeBill.dto.ChargeBillHistoryDTO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.UnitVO;

public interface PaymentsReceiptService {

	public List<ChargeBillHistoryDTO> getChargeBillHistory(ChargeBillHistoryDTO cbhDTO);
	
	public String getRentalPtyId(String mbrCd);
	
	public List<BuildingVO> getOwnBuildings(String rentalPtyId);
	
	public List<UnitVO> getUnits(String bldgId);
}
