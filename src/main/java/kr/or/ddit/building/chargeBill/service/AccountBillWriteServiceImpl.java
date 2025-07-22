package kr.or.ddit.building.chargeBill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.building.chargeBill.dto.ChargeBillCreateDTO;
import kr.or.ddit.building.chargeBill.dto.EnergyUsageDTO;
import kr.or.ddit.building.chargeBill.dto.IntegratedMgmtFeeDTO;
import kr.or.ddit.building.mapper.AccountBillWriteMapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.HouseholdEnergyMonthlyUsageVO;
import kr.or.ddit.vo.IntegratedManagementFeeVO;
import kr.or.ddit.vo.ManagementEntityMonthlyChargeAggregationVO;
import kr.or.ddit.vo.TenancyAccountVO;
import kr.or.ddit.vo.UnitVO;

@Service
public class AccountBillWriteServiceImpl implements AccountBillWriteService {

	@Autowired
	AccountBillWriteMapper mapper;
	

	@Override
	public List<BuildingVO> getOwnBuildings(String mbrCd) {
		// TODO Auto-generated method stub
		return mapper.selectOwnBuildings(mbrCd);
	}

	@Override
	public List<UnitVO> getUnits(String bldgId, String rentalPtyId) {
		// TODO Auto-generated method stub
		return mapper.selectUnits(bldgId, rentalPtyId);
	}

	@Override
	public List<TenancyAccountVO> getAccounts(String mbrCd) {
		// TODO Auto-generated method stub
		return mapper.selectAccounts(mbrCd);
	}

	@Override
	public String getRentalPty(String mbrCd) {
		// TODO Auto-generated method stub
		return mapper.selectRentalPty(mbrCd);
	}

	@Override
	public List<ManagementEntityMonthlyChargeAggregationVO> getOwnUsage(List<String> unitIds) {
		// TODO Auto-generated method stub
		return mapper.selectOwnUsage(unitIds);
	}
	@Transactional
	@Override
	public void createChargeBill(List<ChargeBillCreateDTO> chargeBillList, List<EnergyUsageDTO> energyUsageList,
			List<IntegratedMgmtFeeDTO> intgfeeList) {
		for (ChargeBillCreateDTO dto : chargeBillList) {
		    mapper.insertChargeBill(dto);
		}

		for (EnergyUsageDTO dto : energyUsageList) {
		    mapper.insertEnergyUsage(dto);
		}

		for (IntegratedMgmtFeeDTO dto : intgfeeList) {
		    mapper.insertManagementFee(dto);
		}
		
	}

	
	
	
}
