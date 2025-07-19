package kr.or.ddit.building.chargeBill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.mapper.AccountBillWriteMapper;
import kr.or.ddit.vo.BuildingVO;
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
	public List<ManagementEntityMonthlyChargeAggregationVO> getOwnUsage(String unitId) {
		// TODO Auto-generated method stub
		return mapper.selectOwnUsage(unitId);
	}

	@Override
	public String getRentalPty(String mbrCd) {
		// TODO Auto-generated method stub
		return mapper.selectRentalPty(mbrCd);
	}

}
