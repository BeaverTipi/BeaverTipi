package kr.or.ddit.building.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.building.chargeBill.dto.ChargeBillHistoryDTO;
import kr.or.ddit.building.chargeBill.dto.EnergyUsageDTO;
import kr.or.ddit.building.chargeBill.dto.IntegratedMgmtFeeDTO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChargeBillVO;
import kr.or.ddit.vo.UnitVO;

@Mapper
public interface PaymentsReceiptMapper {
	
	List<Map<String, Object>> selectMonthlyChargeBillSummary(
		    @Param("rentalPtyId") String rentalPtyId,
		    @Param("chgbillChargeMonth") String chgbillChargeMonth
		);
	
	public String selectRentalPtyId(String mbrCd);
	
	public List<BuildingVO> selectOwnBuildings(String rentalPtyId);
	
	public List<UnitVO> selectUnits(
			@Param("bldgId") String bldgId,
			@Param("rentalPtyId") String rentalPtyId);
	
	List<ChargeBillHistoryDTO> selectChargeBillHistoryPaged(
		    @Param("cbh") ChargeBillHistoryDTO cbhDTO,
		    @Param("startRow") int startRow,
		    @Param("endRow") int endRow
		);
	
	public int selectChargeBillHistoryCount(ChargeBillHistoryDTO cbhDTO);
	
	
	public List<EnergyUsageDTO> selectOwnEnergyUsage(@Param("chgbillChargeMonth") String chgbillChargeMonth, @Param("unitId") String unitId);
	
	public List<IntegratedMgmtFeeDTO> selectManagementFee(@Param("chgbillChargeMonth") String chgbillChargeMonth, @Param("unitId") String unitId);
	
	public ChargeBillHistoryDTO selectChargebill(@Param("chgbillChargeMonth") String chgbillChargeMonth, @Param("unitId") String unitId );
	
	public int updateOverdue();
	
}
