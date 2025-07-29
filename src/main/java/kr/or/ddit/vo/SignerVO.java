package kr.or.ddit.vo;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignerVO {

    @NotBlank
    private String contId;

    @NotNull
    @Pattern(regexp = "AGENT|LESSOR|LESSEE")
    private String role;

    @NotBlank
    private String code;

    @NotBlank
    private String id;

    @NotBlank
    private String name;

//    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$")
    private String telno;

    private String signerStatus;
    
    @Pattern(regexp = "^\\d{1,3}(\\.\\d{1,3}){3}$")
    private String ipAddr;

    private Boolean isJoined;

    private LocalDateTime signedAt;
    
    private Boolean isSigned;

    private String hashVal;

    @NotNull
    private Boolean isValid;

    private String base64;

    private String tempPdfUrl;
}