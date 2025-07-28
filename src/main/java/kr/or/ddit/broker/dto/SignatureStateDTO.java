package kr.or.ddit.broker.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignatureStateDTO {

    @NotBlank
    private String contId;

    @NotNull
    private Boolean loading;

    private String error;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<@Valid SignerDTO> signers;

    @Valid
    private SignerDTO signerInfo;

    @Valid
    private PdfMetaData pdfData;

    private String tempPdfUrl;

    private String tempFileId;

    @Valid
    private SignatureStatus signatureStatus;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PdfMetaData {
        @NotBlank
        private String fileName;

        @NotBlank
        private String createdAt; // ISO 8601 string
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignatureStatus {
        private String AGENT;
        private String LESSOR;
        private String LESSEE;
    }
}