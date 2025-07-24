package kr.or.ddit.building.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.building.chargeBill.dto.ChargeBillHistoryDTO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.UnitVO;

@Mapper
public interface PaymentsReceiptMapper {
	
	public List<ChargeBillHistoryDTO> selectChargeBillHistory(ChargeBillHistoryDTO cbhDTO);
	
	public String selectRentalPtyId(String mbrCd);
	
	public List<BuildingVO> selectOwnBuildings(String rentalPtyId);
	
	public List<UnitVO> selectUnits(String bldgId);
}
