package kr.or.ddit.building.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChargeBillVO;
import kr.or.ddit.vo.HouseholdEnergyMonthlyUsageVO;
import kr.or.ddit.vo.IntegratedManagementFeeVO;
import kr.or.ddit.vo.ManagementEntityMonthlyChargeAggregationVO;
import kr.or.ddit.vo.TenancyAccountVO;
import kr.or.ddit.vo.UnitVO;

@Mapper
public interface AccountBillWriteMapper {

	public int insertManagementFee(IntegratedManagementFeeVO imfVO);

	public int insertEnergyUsage(HouseholdEnergyMonthlyUsageVO hemuVO);
	
	public int insertChargeBill(ChargeBillVO cbVO);
	
	public List<BuildingVO> selectOwnBuildings(String mbrCd);
	
	public List<UnitVO> selectUnits(@Param ("bldgId") String bldgId , @Param ("rentalPtyId") String rentalPtyId);
	
	public List<TenancyAccountVO> selectAccounts(String mbrCd);
	
	public List<ManagementEntityMonthlyChargeAggregationVO> selectOwnUsage(String unitId);
	
	public String selectRentalPty(String mbrCd);
}
