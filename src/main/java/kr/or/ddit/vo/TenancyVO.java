package kr.or.ddit.vo;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import kr.or.ddit.util.validate.TenancyInsertGroup;
import kr.or.ddit.util.validate.TenancyUpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 임대인 VO
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
public class TenancyVO extends MemberVO implements Serializable {

    // Insert 시에는 없어도 되고, Update 시에는 필요
    @NotBlank(groups = TenancyUpdateGroup.class)
    @Size(max = 10, groups = TenancyUpdateGroup.class)
    private String rentalPtyId;

    @NotBlank(groups = TenancyUpdateGroup.class)
    @Size(max = 11, groups = TenancyUpdateGroup.class)
    private String mbrCd;

    @NotBlank(message = "사업자 등록 파일 경로는 필수입니다.", groups = TenancyInsertGroup.class)
    @Size(max = 255, groups = {TenancyInsertGroup.class, TenancyUpdateGroup.class})
    private String rentalPtyBizRegFilePath;

    @NotNull(message = "등록 건물 수는 필수입니다.", groups = TenancyInsertGroup.class)
    @Min(value = 0, groups = {TenancyInsertGroup.class, TenancyUpdateGroup.class})
    @Max(value = 999, groups = {TenancyInsertGroup.class, TenancyUpdateGroup.class})
    private Integer rentalPtyRegBldgCnt;

    @NotBlank(message = "사업자등록번호는 필수입니다.", groups = TenancyInsertGroup.class)
    @Size(max = 10, groups = {TenancyInsertGroup.class, TenancyUpdateGroup.class})
    @Pattern(regexp = "^\\d{1,10}$", message = "사업자등록번호는 숫자만 입력해주세요.",
             groups = {TenancyInsertGroup.class, TenancyUpdateGroup.class})
    private String rentalPtyBizRegNo;

    @Size(max = 255, groups = {TenancyInsertGroup.class, TenancyUpdateGroup.class})
    private String rentalPtyFile;

    @Size(max = 255, groups = {TenancyInsertGroup.class, TenancyUpdateGroup.class})
    private String rentalPtyFile2;

    @NotBlank(message = "임대유형 구분은 필수입니다.", groups = TenancyInsertGroup.class)
    @Size(max = 12, groups = {TenancyInsertGroup.class, TenancyUpdateGroup.class})
    private String lsrYnTypeCd;

    @Size(max = 5, groups = {TenancyInsertGroup.class, TenancyUpdateGroup.class})
    private String lsrTypeGroupCd;

    @Pattern(regexp = "^[YN]$", message = "승인 여부는 'Y' 또는 'N'이어야 합니다.",
             groups = {TenancyInsertGroup.class, TenancyUpdateGroup.class})
    private String authApprYn;

    private MultipartFile tenancyFile;
}
