package kr.or.ddit.building.chargeBill.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnergyUsageDTO {
    private String unitId;
    private String bldgId;
    private String rentalPtyId;
    private String dumComp;
    private Double totalEnergyUsageQty;
    private Integer totalEnergyChargeAmt;
}
