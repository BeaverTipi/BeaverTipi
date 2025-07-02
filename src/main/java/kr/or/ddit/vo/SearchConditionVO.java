package kr.or.ddit.vo;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import kr.or.ddit.util.validate.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "mbrCd")
public class SearchConditionVO implements Serializable {
	@NotBlank(groups = UpdateGroup.class)
	private String mbrCd;
	private String userRoleId;
    private String mbrId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mbrFrstRegDtFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mbrFrstRegDtTo;
    private String mbrStatusCode;
    private String mbrEmlAddr;

    // 기본 생성자
    public SearchConditionVO() {}

    // 모든 필드를 포함하는 생성자 (선택 사항)
    public SearchConditionVO(String userRoleId, String mbrId, LocalDate mbrFrstRegDtFrom,
                             LocalDate mbrFrstRegDtTo, String mbrStatusCode, String mbrEmlAddr) {
        this.userRoleId = userRoleId;
        this.mbrId = mbrId;
        this.mbrFrstRegDtFrom = mbrFrstRegDtFrom;
        this.mbrFrstRegDtTo = mbrFrstRegDtTo;
        this.mbrStatusCode = mbrStatusCode;
        this.mbrEmlAddr = mbrEmlAddr;
    }
    
    @Override
    public String toString() {
        return "SearchConditionVO [userRoleId=" + userRoleId + ", mbrId=" + mbrId + ", mbrFrstRegDtFrom="
                + mbrFrstRegDtFrom + ", mbrFrstRegDtTo=" + mbrFrstRegDtTo + ", mbrStatusCode=" + mbrStatusCode
                + ", mbrEmlAddr=" + mbrEmlAddr + "]";
    }
}
