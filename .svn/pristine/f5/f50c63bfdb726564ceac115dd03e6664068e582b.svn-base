package kr.or.ddit.vo;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List; // java.util.List 임포트

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import kr.or.ddit.util.validate.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = "mbrCd")
@NoArgsConstructor
@AllArgsConstructor
public class SearchConditionVO implements Serializable {
    @NotBlank(groups = UpdateGroup.class)
    private String mbrCd;

    // userRoleId를 List<String>으로 변경 (여러 역할을 받을 수 있도록)
    private List<String> userRoleId;

    private String mbrId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mbrFrstRegDtFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mbrFrstRegDtTo;
    private String mbrStatusCode;
    private String mbrEmlAddr;

}