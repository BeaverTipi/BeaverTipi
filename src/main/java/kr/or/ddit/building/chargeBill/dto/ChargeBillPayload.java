package kr.or.ddit.building.chargeBill.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeBillPayload {
    private List<ChargeBillCreateDTO> chargeBillList;
    private List<EnergyUsageDTO> energyUsageList;
    private List<IntegratedMgmtFeeDTO> intgfeeList;
}

