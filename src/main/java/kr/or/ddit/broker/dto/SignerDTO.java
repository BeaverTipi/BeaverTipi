package kr.or.ddit.broker.dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignerDTO {

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

    @Pattern(regexp = "^\\d{1,3}(\\.\\d{1,3}){3}$")
    private String ipAddr;

    @NotNull
    @Pattern(regexp = "JOINED|SIGNED|REJECTED")
    private String status;

    private LocalDateTime signedAt;

    private String hashVal;

    @NotNull
    private Boolean isValid;

    @NotNull
    private Boolean isRejected;

    private String base64;

    private String tempPdfUrl;

    private Boolean connected;
}