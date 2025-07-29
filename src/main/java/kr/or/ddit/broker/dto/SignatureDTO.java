package kr.or.ddit.broker.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignatureDTO {

    private String contId;
    private String signatureStatus;
    private String originalPdfData;
    private String lessorSignedPdfData;
    private String lessorSignedPdfId;
    private String lessorSignedPdfPath;
    private String lesseeSignedPdfData;
    private String lesseeSignedPdfId;
    private String lesseeSIgnedPdfPath;
    private String agentSignedPdfData;
    private String agendSignedPdfId;
    private String agentSignedPdfPath;
}
