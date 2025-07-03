package kr.or.ddit.vo;

import java.io.Serializable;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
public class TenancyVO extends MemberVO implements Serializable {
	@NotBlank @Size(max = 10)
		private String rentalPtyId;
	@NotBlank @Size(max = 255)
		private String rentalPtyBizRegFilePath;
	@NotNull @Min(0) @Max(999)
		private int rentalPtyRegBldgCnt;
	@NotBlank @Size(max = 20) @Pattern(regexp = "^\\d{1,20}$", message = "계좌번호는 숫자만 입력해주세요.")
		private String rentalPtyAcctNo;
	@NotBlank @Size(max = 10) @Pattern(regexp = "^\\d{1,10}$", message = "사업자등록번호는 숫자만 입력해주세요.")
		private String rentalPtyBizRegNo;
		private String rentalPtyFile;
		private String rentalPtyFile2;
	@NotBlank @Size(max = 5)
		private String lsrYnTypeCd;
	@NotBlank @Size(max = 100)
		private String rentalPtyBankNm;
	@Size(max = 5)
		private String lsrTypeGroupCd;
}
