package kr.or.ddit.vo;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author developer_KCY
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(of = "rentalPtyId")
public class TenancyVO implements Serializable {
	@NotBlank @Size(max = 10)
		private String rentalPtyId;
	@NotBlank @Size(max = 11)
		private String mbrCd;
	@NotBlank @Size(max = 255)
		private String rentalPtyBizRegFilePath;
	@NotNull @Min(0) @Max(999)
		private Integer rentalPtyRegBldgCnt;
	@NotBlank @Size(max = 20) @Pattern(regexp = "^\\d{1,20}$", message = "계좌번호는 숫자만 입력해주세요.")
		private String rentalPtyAcctNo;
	@NotBlank @Size(max = 10) @Pattern(regexp = "^\\d{1,10}$", message = "사업자등록번호는 숫자만 입력해주세요.")
		private String rentalPtyBizRegNo;
	@Size(max = 255)
		private String rentalPtyFile;
	@Size(max = 255)
		private String rentalPtyFile2;
	@NotBlank @Size(max = 12)
		private String lsrYnTypeCd;
	@Size(max = 5)
		private String lsrTypeGroupCd;
	@NotBlank @Size(max = 100)
		private String rentalPtyBankNm;
	@Pattern(regexp = "^[YN]$", message = "승인 여부는 'Y' 또는 'N'이어야 합니다.")
    	private String authApprYn;
	
	private MultipartFile tenancyFile;
}
