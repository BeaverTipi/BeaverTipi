package kr.or.ddit.vo;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"accNum", "bldgId", "rentalPtyId"})
@SuppressWarnings("serial")
public class TenancyAccountVO implements Serializable{
    
	@NotBlank
    @Size(max = 20)
    private String accNum;

    @NotBlank
    @Size(max = 30)
    private String accBank;

    @NotBlank
    @Size(max = 10)
    private String accMaster;

    @NotBlank
    @Size(max = 11)
    private String bldgId;

    @NotBlank
    @Size(max = 10)
    private String rentalPtyId;

    @NotBlank
    @Size(max = 11)
    private String mbrCd;

    @Pattern(regexp = "\\d{8}")
    private String accRegDate;

    @Pattern(regexp = "[YN]")
    private String accDelYn;
    
    private BuildingVO building;
}
