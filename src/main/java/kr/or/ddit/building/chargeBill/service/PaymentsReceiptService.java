package kr.or.ddit.building.chargeBill.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.building.chargeBill.dto.ChargeBillHistoryDTO;
import kr.or.ddit.building.chargeBill.dto.EnergyUsageDTO;
import kr.or.ddit.building.chargeBill.dto.IntegratedMgmtFeeDTO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChargeBillVO;
import kr.or.ddit.vo.UnitVO;

public interface PaymentsReceiptService {

	public List<Map<String, Object>> getMonthlyChargeBillSummary(String rentalPtyId, String chgbillChargeMonth);

    public int getChargeBillHistoryCount(ChargeBillHistoryDTO cbhDTO);  // 페이징용

    public List<ChargeBillHistoryDTO> getChargeBillHistoryPaged(ChargeBillHistoryDTO cbhDTO, int startRow, int endRow);

    public String getRentalPtyId(String mbrCd);

    public List<BuildingVO> getOwnBuildings(String rentalPtyId);

    public List<UnitVO> getUnits(String bldgId, String rentalPtyId);
    
    public List<EnergyUsageDTO> getEnergyUsage(String chgbillChargeMonth, String unitId);
    
    public List<IntegratedMgmtFeeDTO> getManagementFee(String chgbillChargeMonth, String unitId);
    
    public ChargeBillHistoryDTO getChargebill(String chgbillChargeMonth, String unitId);
    
    public void modifyOverdue();
}
