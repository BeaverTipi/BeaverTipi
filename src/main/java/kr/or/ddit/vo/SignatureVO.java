package kr.or.ddit.vo;


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
public class SignatureVO {

    private String contId;
    private String signatureStatus;
    private String originalPdfData;
    private String lessorSignedPdfData;
    private String lessorSignedPdfId;
    private String lessorSignedPdfPath;
    private String lesseeSignedPdfData;
    private String lesseeSignedPdfId;
    private String lesseeSignedPdfPath;
    private String agentSignedPdfData;
    private String agentSignedPdfId;
    private String agentSignedPdfPath;
}
