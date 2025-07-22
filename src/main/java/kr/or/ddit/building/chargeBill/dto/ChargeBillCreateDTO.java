package kr.or.ddit.building.chargeBill.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeBillCreateDTO {
    private String rentalPtyId;
    private String unitId;
    private String bldgId;
    private Long chgbillAmount;


    private String chgbillDueDate;

    private String chgbillDesc;
    private String chgbillAccNum;
}