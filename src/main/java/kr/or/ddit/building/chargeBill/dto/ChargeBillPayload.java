package kr.or.ddit.building.chargeBill.dto;

import java.io.Serializable;
import java.util.List;

import kr.or.ddit.vo.HouseholdEnergyMonthlyUsageVO;
import kr.or.ddit.vo.IntegratedManagementFeeVO;
import lombok.Data;

@Data
public class ChargeBillPayload implements Serializable{
    private List<ChargeBillCreateDTO> chargeBillList;
    private List<HouseholdEnergyMonthlyUsageVO> energyUsageList;
    private List<IntegratedManagementFeeVO> intgfeeList;
}

