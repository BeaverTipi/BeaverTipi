package kr.or.ddit.vo;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author developer_KCY 
 * 
 */
@Data
@EqualsAndHashCode(of= {"solId"})
public class SolutionVO implements Serializable{
	@NotBlank(message = "솔루션 ID는 필수입니다.") @Size(max = 8)
    private String solId;
    @Size(max = 100)
    private String solName;
    private String solDesc;
    @NotNull(message = "체험 기간은 필수입니다.")
    private Integer solTrialDur;
    @NotNull(message = "가격은 필수입니다.")
    private Integer solPrice;
    @NotNull(message = "구독 주기는 필수입니다.")
    private Integer solCycle;
    @NotBlank(message = "활성 여부는 필수입니다.") @Pattern(regexp = "^[YN]$", message = "활성 여부는 'Y' 또는 'N'만 가능합니다.")
    private String solIsActive;
    @Size(max = 5)
    private String solActiveGrpCd;
    @Pattern(regexp = "^[YN]$", message = "승인 여부는 'Y' 또는 'N'만 가능합니다.")
    private String subsApprovalYn = "N";
    @Size(max = 5)
    private String solGrpCd;
    private String solCcCd;
}
