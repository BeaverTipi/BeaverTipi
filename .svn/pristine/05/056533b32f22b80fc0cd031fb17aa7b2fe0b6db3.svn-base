package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author developer_KCY
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
public class BrokerVO extends MemberVO implements Serializable {
    @NotBlank(message = "회원 코드(MBR_CD)는 필수입니다.") @Size(max = 11)
    	private String mbrCd;
    @Size(max = 200)
    	private String brokNm;
    @Size(max = 10) @Pattern(regexp = "^\\d{10}$", message = "사업자등록번호는 숫자 10자리여야 합니다.")
    	private String brokRegNo;
    @Size(max = 50)
    	private String crtfNo;
    @Size(max = 100)
    	private String reprNm;
    @Size(max = 20) /*@Pattern(regexp = "^\\d{2,4}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다 (예: 010-1234-5678)")*/
    	private String reprTelNo;
    	private LocalDateTime regDtm;
    @Size(max = 400)
    	private String brokAddr2;
    private String brokZip;
    @Size(max = 255)
    	private String brokAddr1;
    @Pattern(regexp = "^[YN]$", message = "권한 승인 여부는 'Y' 또는 'N'만 가능합니다.")
    	private String authApprYn = "N";
    
    private MultipartFile brokFile;
}
