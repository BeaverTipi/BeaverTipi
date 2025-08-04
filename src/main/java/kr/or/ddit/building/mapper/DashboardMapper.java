package kr.or.ddit.building.mapper;

import java.util.List;

import kr.or.ddit.building.chargeBill.dto.EnergyUsageDTO;
import kr.or.ddit.building.main.dto.ChargeBillCountDTO;

public interface DashboardMapper {

	public int countResident(String bldgId);
	
	public int countVacancy(String bldgId);
	
	public int countRentalRoom(String bldgId);
	
	public List<EnergyUsageDTO> countEnergy(String bldgId);
	
	public List<EnergyUsageDTO> countLastMonthEnergy(String bldgId);
	
	public List<EnergyUsageDTO> countLastYearEnergy(String bldgId);
	
	public List<ChargeBillCountDTO> selectCurrentChargeBillStatus(String bldgId);
	
	public List<ChargeBillCountDTO> selectLastMonthChargeBillStatus(String bldgId);
	
	
}
