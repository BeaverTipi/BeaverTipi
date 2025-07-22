package kr.or.ddit.building.chargeBill.service;

import java.util.List;

import kr.or.ddit.building.chargeBill.dto.ChargeBillCreateDTO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.HouseholdEnergyMonthlyUsageVO;
import kr.or.ddit.vo.IntegratedManagementFeeVO;
import kr.or.ddit.vo.ManagementEntityMonthlyChargeAggregationVO;
import kr.or.ddit.vo.TenancyAccountVO;
import kr.or.ddit.vo.UnitVO;

public interface AccountBillWriteService {

	public List<BuildingVO> getOwnBuildings(String mbrCd);
	
	public List<UnitVO> getUnits(String bldgId, String rentalPtyId);
	
	public List<TenancyAccountVO> getAccounts(String mbrCd);
	
	public List<ManagementEntityMonthlyChargeAggregationVO> getOwnUsage(List<String> unitIds);
	
	public String getRentalPty(String mbrCd);
	
	public void createChargeBill(
			List<ChargeBillCreateDTO> chargeBillList,
			List<HouseholdEnergyMonthlyUsageVO> energyUsageList,
			List<IntegratedManagementFeeVO> intgfeeList);
}
