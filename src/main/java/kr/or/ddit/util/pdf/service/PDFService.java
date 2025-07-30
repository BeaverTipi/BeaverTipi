package kr.or.ddit.util.pdf.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.DocumentException;

import kr.or.ddit.util.pdf.SignerRole;

public interface PDFService {
	public Map<String, Object> extractCrtfNo(MultipartFile file);
    public Map<String, Object> extractBizRegNo(MultipartFile file);
    public File mergeToSinglePdf(List<MultipartFile> files) throws Exception;
    public byte[] insertMultipleSignaturesToPDF(
    		byte[] originalPdfBytes,
            List<SignatureInfo> signatureInfos)
            throws IOException, DocumentException;
    List<SignaturePosition> getPositionForRole(SignerRole role);
    public static class SignaturePosition {
        public int pageNumber;
        public float x;
        public float y;
        public float width;
        public float height;

        public SignaturePosition(int pageNumber, float x, float y, float width, float height) {
            this.pageNumber = pageNumber;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
    // 개별 서명자 정보 클래스
    public static class SignatureInfo {
        public SignerRole role;
        public byte[] imageBytes;
        public int pageNumber;
        public float x, y, width, height;

        public SignatureInfo(SignerRole role, byte[] imageBytes,
                             int pageNumber, float x, float y,
                             float width, float height) {
            this.role = role;
            this.imageBytes = imageBytes;
            this.pageNumber = pageNumber;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}
