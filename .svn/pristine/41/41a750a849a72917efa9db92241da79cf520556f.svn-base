package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kr.or.ddit.util.validate.BrokerInsertGroup;
import kr.or.ddit.util.validate.BrokerUpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 중개사 VO
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
public class BrokerVO extends MemberVO implements Serializable {

    // Insert 시에는 검증 제외, Update 시에만 필수
    @NotBlank(message = "회원 코드(MBR_CD)는 필수입니다.", groups = BrokerUpdateGroup.class)
    @Size(max = 11, groups = BrokerUpdateGroup.class)
    private String mbrCd;

    @NotBlank(message = "사무소명은 필수입니다.", groups = BrokerInsertGroup.class)
    @Size(max = 200, groups = {BrokerInsertGroup.class, BrokerUpdateGroup.class})
    private String brokNm;

    @NotBlank(message = "사업자등록번호는 필수입니다.", groups = BrokerInsertGroup.class)
    @Size(max = 10, groups = {BrokerInsertGroup.class, BrokerUpdateGroup.class})
    @Pattern(regexp = "^\\d{10}$", message = "사업자등록번호는 숫자 10자리여야 합니다.",
             groups = {BrokerInsertGroup.class, BrokerUpdateGroup.class})
    private String brokRegNo;

    @Size(max = 50, groups = {BrokerInsertGroup.class, BrokerUpdateGroup.class})
    private String crtfNo;

    @NotBlank(message = "대표자명은 필수입니다.", groups = BrokerInsertGroup.class)
    @Size(max = 100, groups = {BrokerInsertGroup.class, BrokerUpdateGroup.class})
    private String reprNm;

    @NotBlank(message = "대표 전화번호는 필수입니다.", groups = BrokerInsertGroup.class)
    @Size(max = 20, groups = {BrokerInsertGroup.class, BrokerUpdateGroup.class})
    private String reprTelNo;

    private LocalDateTime regDtm;

    @Size(max = 400, groups = {BrokerInsertGroup.class, BrokerUpdateGroup.class})
    private String brokAddr2;

    private String brokZip;

    @NotBlank(message = "주소는 필수입니다.", groups = BrokerInsertGroup.class)
    @Size(max = 255, groups = {BrokerInsertGroup.class, BrokerUpdateGroup.class})
    private String brokAddr1;

    @Pattern(regexp = "^[YN]$", message = "권한 승인 여부는 'Y' 또는 'N'만 가능합니다.",
             groups = {BrokerInsertGroup.class, BrokerUpdateGroup.class})
    private String authApprYn = "N";

    private MultipartFile brokFile;
}
